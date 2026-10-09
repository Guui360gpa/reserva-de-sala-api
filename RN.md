# Regras de Negócio

## RN01: Sala inativa não pode ser reservada

**Descrição:** Uma sala com ativa = false não aceita novas reservas.

**Motivo:** Salas podem estar em manutenção, interditadas ou desativadas administrativamente.

**Detalhes:**

- Salas são desativadas (soft delete) em vez de removidas, preservando o histórico de reservas.
- Desativar uma sala não cancela automaticamente as reservas futuras já existentes. Essa decisão precisa ser documentada: ou elas são mantidas, ou o sistema notifica/cancela (sugestão para versão futura).
- Salas inativas não aparecem na busca de disponibilidade.

**Exemplo:** Sala “Auditório B” está inativa → POST /reservas com essa sala retorna erro.

**Erro:** 422 Unprocessable Entity (SalaInativaException)

## RN02: Capacidade da sala deve ser positiva

**Descrição:** O campo capacidade de uma sala deve ser um inteiro maior que zero.

**Detalhes:**

- Vale tanto no cadastro quanto na atualização.
- Se existir um limite máximo razoável (ex.: 500), vale registrá-lo como regra.
A- o reduzir a capacidade de uma sala, reservas futuras que excedam o novo valor ficam inconsistentes. Sugestão: bloquear a redução nesse caso.

**Exemplo:** capacidade: 0 ou capacidade: -5 → rejeitado.

**Erro:** 400 Bad Request (validação de entrada com @Positive)

## RN03: Data de fim posterior à data de início

**Descrição:** dataFim deve ser estritamente maior que dataInicio.

**Detalhes:**

- Igualdade também é inválida (dataFim == dataInicio gera duração zero).
- Início e fim devem estar no mesmo dia? Defina isso. Se reservas podem atravessar a meia-noite, a regra muda.
- Recomenda-se não permitir reservas no passado (dataInicio deve ser futura).

**Exemplo:** início 14:00, fim 13:00 → rejeitado.

**Erro:** 400 Bad Request (ou 422, se preferir tratar como regra de domínio)

## RN04: Duração mínima de 30 minutos

**Descrição:** A diferença entre dataFim e dataInicio deve ser maior ou igual a 30 minutos.

**Detalhes:**

- Calculada com Duration.between(inicio, fim).toMinutes() >= 30.
- Opcionalmente, defina uma duração máxima (ex.: 8 horas) e granularidade (horários só em múltiplos de 15 ou 30 min, como 14:00, 14:30).

**Exemplos:**

- 14:00-14:30	Válido
- 14:00-14:15	Rejeitado
- 14:00-14:29	Rejeitado

**Erro:** 422 Unprocessable Entity (DuracaoMinimaException)

## RN05: Pessoas não podem ultrapassar a capacidade

**Descrição:** quantidadePessoas ≤ sala.capacidade.

**Detalhes:**

- Além disso, quantidadePessoas deve ser positivo (mínimo 1).
- Esta regra depende dos dados da sala, por isso é validação de domínio e não pode ficar só no DTO.

**Exemplo:** Sala com capacidade 10, reserva com 12 pessoas → rejeitada.

**Erro:** 422 Unprocessable Entity (CapacidadeExcedidaException)

## RN06: Ocupação mínima exigida (capacidade mínima)

**Descrição:** Para evitar que salas grandes sejam usadas por poucas pessoas, a reserva só é aceita se a quantidade de pessoas atingir uma ocupação mínima, calculada a partir da capacidade da sala.

**Fórmula sugerida:**

`capacidadeMinima = ceil(capacidade × percentualMinimo)`

Detalhes:

- Use Math.ceil para arredondar para cima, garantindo que o mínimo nunca seja subestimado.
- Deixe os percentuais configuráveis (application.properties ou enum/classe de política), em vez de fixos no código.
- A regra completa fica: capacidadeMinima ≤ quantidadePessoas ≤ capacidade.

**Exemplo:** Sala de 20 lugares com 30% de ocupação mínima → mínimo 6 pessoas. Reserva com 3 pessoas é rejeitada.

**Erro:** 422 Unprocessable Entity (OcupacaoMinimaNaoAtendidaException)

## RN07: Sem sobreposição de reservas na mesma sala

**Descrição:** Uma sala não pode ter duas reservas ativas com períodos que se cruzam.

**Condição de sobreposição:**

inicioNovo < fimExistente  E  fimNovo > inicioExistente

**Detalhes:**

- Reservas canceladas são ignoradas na verificação.
- Reservas adjacentes são permitidas: uma termina às 15:00 e outra começa às 15:00 não conflitam.
- Se necessário, considere um intervalo de limpeza/preparação entre reservas (ex.: 10 min). Seria uma extensão da regra.
- Ao editar uma reserva, ela deve ser excluída da verificação (para não conflitar consigo mesma).
Atenção à concorrência: duas requisições simultâneas podem passar na checagem. Use lock pessimista ou constraint de exclusão no banco.

**Exemplos (sala já reservada de 14:00 às 16:00):**

- 13:00 – 14:00	Válido (adjacente)
- 16:00 – 17:00	Válido (adjacente)
- 15:00 – 17:00	Conflito
- 13:00 – 15:00	Conflito
- 14:30 – 15:30	Conflito (contido)
- 13:00 – 17:00	Conflito (envolve)

**Erro:** 409 Conflict (ConflitoDeHorarioException)

## RN08: Não permitir reservas com início no passado

**Descrição:** dataInicio deve ser posterior ao momento atual da requisição.

**Motivo:** Reservar um horário que já passou não tem utilidade e pode ser usado para fraudar relatórios de ocupação.

**Detalhes:**

- Compare sempre com o relógio do servidor, nunca com um horário enviado pelo cliente.
- Injete um Clock no service em vez de chamar LocalDateTime.now() direto. Isso permite testar a regra com horários fixos.
- Defina uma tolerância (ex.: 5 minutos) caso queira permitir reservas “para agora”. Sem tolerância, uma requisição às 14:00:03 com início às 14:00 seria rejeitada.
- Cuidado com fuso horário: o servidor e o usuário podem estar em zonas diferentes. Defina um fuso padrão (ex.: America/Sao_Paulo) ou use OffsetDateTime/Instant.
- Vale na criação e na edição de reservas.

**Erro:** 422 Unprocessable Entity (DataNoPassadoException). No DTO, @Future cobre a checagem básica, mas o domínio deve repetir a regra por segurança.


## RN09: Apenas o criador da reserva (ou um administrador) pode cancelá-la

**Descrição:** O cancelamento só é permitido ao usuário que criou a reserva ou a um usuário com perfil de administrador.

**Motivo:** Impede que um usuário derrube a reserva de outro, o que geraria conflitos e desconfiança no sistema.

**Detalhes:**

- Exige um conceito de perfil/papel no usuário (USUARIO, ADMIN), que pode ser um enum no início.
- A identidade de quem solicita deve vir da autenticação (token/sessão), nunca de um parâmetro enviado no corpo da requisição. Caso contrário, qualquer um poderia se passar por outro. Até implementar Spring Security, você pode simular com um header, mas deixe documentado como solução provisória.
- A comparação deve ser feita pelo ID do usuário, não por equals em objetos carregados separadamente (a menos que você tenha implementado equals/hashCode corretamente).
- O cancelamento deve ser lógico (muda o status para CANCELADA), preservando o histórico.
- Considere registrar quem cancelou e quando (canceladaPor, canceladaEm). Isso ajuda em auditoria, principalmente quando um admin cancela a reserva de terceiros.

**Exemplos:**

- Ana (usuário)	Ana	Permitido
- Bruno (usuário)	Ana	Negado
- Carlos (admin)	Ana	Permitido

**Erro:** 403 Forbidden (AcessoNegadoException)

## RN10: Reserva já cancelada não pode ser cancelada novamente

**Descrição:** Só reservas com status ATIVA podem ser canceladas.

**Motivo:** Evita operações redundantes, mantém o histórico limpo (a data de cancelamento não é sobrescrita) e deixa explícito para o cliente da API que algo está errado.

**Detalhes:**

- Isso é uma máquina de estados simples: ATIVA → CANCELADA. Não existe caminho de volta. Se quiser permitir reativar, trate como uma nova reserva (reverificando conflitos).
- Se você evoluir o sistema, novos estados podem surgir (CONCLUIDA, EM_ANDAMENTO, NAO_COMPARECEU). Defina desde já quais transições são válidas.
- Reservas já finalizadas (dataFim no passado) também não deveriam ser canceláveis, pois não há mais o que liberar. Vale incluir isso na regra.
- Decisão de design: o cancelamento é idempotente (retorna sucesso se já estava cancelada) ou estrito (retorna erro)? A regra original adota o modelo estrito, que é mais explícito.


**Erro:** 409 Conflict ou 422 Unprocessable Entity (ReservaNaoCancelavelException)


## RN11: Antecedência mínima e máxima para reservar

**Descrição:** Uma reserva só pode ser criada respeitando uma janela de tempo entre o momento da requisição e o início da reserva.

**Motivo:**

**Antecedência mínima:** dá tempo para preparar a sala (limpeza, equipamentos) e evita reservas de última hora que atrapalham a operação.
**Antecedência máxima:** impede que alguém bloqueie salas com meses ou anos de antecedência, o que prejudica os demais usuários.

**Detalhes:**

- Valores sugeridos como ponto de partida: mínima de 15 a 30 minutos, máxima de 90 dias.
- Deixe os valores configuráveis (application.properties), pois variam por organização.
- Pode haver regras diferentes por perfil: administradores podem ignorar a antecedência mínima.
- Esta regra complementa a RN08. A RN08 diz “não pode ser no passado”, e a RN11 diz “precisa estar dentro da janela aceitável”. Você pode unificá-las em uma só validação.

## RN12: Duração máxima da reserva

**Descrição:** O período de uma reserva não pode ultrapassar um limite máximo (ex.: 4 ou 8 horas).

**Motivo:** Complementa a duração mínima (RN04). Evita que uma única pessoa monopolize uma sala por um dia inteiro ou mais.

**Detalhes:**

- Valor sugerido: 4 horas para salas de reunião, ou 8 horas para salas de uso geral.
- Configurável, e possivelmente diferente por sala (um auditório pode permitir mais tempo que uma sala pequena). Nesse caso, o campo duracaoMaximaMinutos passa a fazer parte da entidade Sala.
- Defina se reservas podem atravessar a meia-noite. Se não, adicione a regra “início e fim no mesmo dia”.
- Para necessidades maiores (ex.: evento de dois dias), a saída é criar reservas separadas ou ter um fluxo de aprovação de administrador.
- Junto com RN04, forma a regra completa: 30 min ≤ duração ≤ duração máxima.