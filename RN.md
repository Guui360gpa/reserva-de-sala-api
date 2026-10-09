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

# Regras a serem analisadas

- RN08: Não permitir reservas com início no passado.
- RN09: Só o criador da reserva (ou um administrador) pode cancelá-la.
- RN10: Uma reserva já cancelada não pode ser cancelada novamente.
- RN11: Definir antecedência mínima e/ou máxima para reservar (ex.: no máximo 90 dias à frente).
- RN12: Definir duração máxima da reserva.