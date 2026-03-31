# ⚔️ KaosPractice - The Ultimate PvP Training Suite

O **KaosPractice** é o motor definitivo para servidores de treinamento e duelos (Practice). Desenvolvido para a versão 1.8.8, ele integra gerenciamento de arenas, sistemas de filas (Queue) e estatísticas competitivas em uma única solução de ultra performance, totalmente integrada ao ecossistema Kaos.

---

## 🚀 Diferenciais Técnicos

### 1. Motor de Instâncias de Arena
O KaosPractice utiliza um sistema de gerenciamento de mundos otimizado para suportar centenas de duelos simultâneos:
* **Fast-Chunk Reset:** Restauração instantânea de arenas após o término dos duelos, garantindo que o próximo par de jogadores encontre o mapa intacto.
* **Integração SlimeWorldManager:** Carregamento de mapas via RAM através da `SlimeWorldController` do KaosCore, eliminando gargalos de I/O de disco.
* **Arena Detection:** Sistema inteligente que detecta automaticamente limites de arena e pontos de spawn para agilizar o setup.

### 2. Matchmaking e Combate
* **Elo-Rating System:** Algoritmo de rankeamento competitivo que separa jogadores por nível de habilidade (Ranked vs Unranked).
* **Spectator Mode:** Sistema de espectador com latência zero, permitindo que staffs e jogadores assistam partidas em tempo real.
* **Kit Management:** Criação de kits complexos (NoDebuff, Combo, Gapple) com suporte a poções, efeitos de poção persistentes e armaduras inquebráveis.

---

## 🛠️ Comandos de Administração

| Comando | Descrição |
| :--- | :--- |
| `/arena create <nome> <tipo>` | Registra uma nova arena no sistema. |
| `/arena setspawn <arena> <1\|2>` | Define os pontos de nascimento dos duelistas na arena. |
| `/arena list` | Lista todas as arenas configuradas e seus status atuais. |
| `/kit create <nome>` | Cria um novo kit de duelo baseado no seu inventário atual. |
| `/kit edit <nome>` | Abre o editor interativo para modificar itens e propriedades do kit. |
| `/kit icon <nome>` | Define o item que aparecerá no menu de seleção de duelos. |
| `/stats reset [jogador]` | Limpa o histórico de vitórias, derrotas e ELO de um usuário. |
| `/inventory <jogador>` | Visualiza o inventário final de um jogador após o término do duelo. |

---

## 📦 Instalação e Configuração

1. Certifique-se de ter o **KaosCore** instalado e configurado corretamente.
2. Insira o arquivo `KaosPractice.jar` na pasta `/plugins` do seu servidor.
3. Configure a conexão com o banco de dados MySQL no arquivo `config.yml` para persistência de estatísticas.
4. Utilize o comando `/arena create` para iniciar a configuração dos seus mapas de treinamento.

---

## 💎 Desenvolvido por KnowPlugins
O **KaosPractice** é um produto de elite da **KnowPlugins**. Focado em entregar a melhor infraestrutura para servidores de Practice, BoxPvP e servidores competitivos.