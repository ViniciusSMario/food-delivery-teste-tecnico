# Food Delivery API

Sistema de pedidos de delivery com autenticação JWT, API REST em Spring Boot e frontend em React. Desenvolvido como desafio técnico.

O usuário autenticado é o administrador do sistema (quem gerencia os pedidos), não o cliente final — o cliente não faz login. Um administrador se cadastra, autentica-se e passa a criar pedidos em nome de um cliente (informado por nome no próprio pedido), listar todos os pedidos do sistema, visualizar um pedido específico e atualizar seu status. Qualquer administrador autenticado enxerga e gerencia todos os pedidos — é um painel compartilhado, não um espaço isolado por usuário.

## Tecnologias

### Backend

- Java 21
- Spring Boot 3.5.6
- Maven
- Spring Security
- Spring Data JPA
- SQLite (`sqlite-jdbc` + `hibernate-community-dialects`)
- JWT (`io.jsonwebtoken` / jjwt 0.12.6)
- Bean Validation
- Lombok

### Frontend

- React 19
- Vite
- Axios
- React Router
- Material UI

## Estrutura do projeto

Monorepo com backend e frontend em pastas separadas:

```
food-delivery-api/
├── backend/
│   ├── src/main/java/com/vinicius/food_delivery_api/
│   │   ├── config/          # SecurityConfig (JWT, CORS, PasswordEncoder)
│   │   ├── controller/      # AuthController, PedidoController
│   │   ├── dto/             # Requests e Responses (nunca expõe entidades)
│   │   ├── entity/          # Usuario, Pedido, ItemPedido, StatusPedido
│   │   ├── exception/       # Exceções de negócio + GlobalExceptionHandler
│   │   ├── repository/      # Interfaces JpaRepository
│   │   ├── security/        # JwtService, JwtFilter, UsuarioDetailsService, EntryPointNaoAutenticado
│   │   └── service/         # AuthService, PedidoService
│   └── src/main/resources/application.properties
│
└── frontend/
    └── src/
        ├── constants/        # STATUS_PEDIDO e seus rótulos/cores
        ├── layouts/          # LayoutPublico (login/cadastro), LayoutPrincipal (área logada)
        ├── pages/
        │   └── pedidos/      # ListaPedidos, NovoPedido, DetalhePedido + components/
        ├── routes/           # AppRoutes, RotaPrivada (guarda de autenticação)
        ├── services/         # api.js (axios + interceptors), auth.js, pedidos.js, token.js
        └── utils/
```

## Como executar o backend

Pré-requisitos: JDK 21.

```bash
cd backend
./mvnw spring-boot:run        # Linux/macOS
.\mvnw.cmd spring-boot:run     # Windows
```

A API sobe em `http://localhost:8080`. O banco SQLite (`food-delivery.db`) é criado automaticamente na primeira execução, na própria pasta `backend/`, junto com as tabelas (`ddl-auto=update`).

### Variáveis de ambiente (opcionais)

| Variável | Padrão (dev) | Descrição |
|---|---|---|
| `JWT_SECRET` | valor de desenvolvimento embutido | Chave usada para assinar os tokens JWT |
| `CORS_ORIGINS` | `http://localhost:5173,http://localhost:5174` | Origens do frontend autorizadas a chamar a API |
| `SPRING_PROFILES_ACTIVE` | `dev` | Perfil ativo do Spring. O perfil `dev` (`application-dev.properties`) liga o log de SQL no console — em outros ambientes, defina um valor diferente para desativar esse log |

Os padrões funcionam sem nenhuma configuração extra para rodar localmente. Em qualquer ambiente que não seja desenvolvimento local, defina `JWT_SECRET` explicitamente — a chave de exemplo está neste repositório público e não deve assinar tokens reais.

## Como executar o frontend

Pré-requisitos: Node.js 18+.

```bash
cd frontend
npm install
npm run dev
```

Abre em `http://localhost:5173` (ou na próxima porta livre, como `5174`, caso a 5173 já esteja em uso).

O endereço da API é lido de `VITE_API_URL` (arquivo `.env`, já configurado como `http://localhost:8080`). Ajuste esse valor — e a lista `CORS_ORIGINS` no backend — caso o frontend rode em outra porta.

## Credenciais de teste

Não há usuário pré-cadastrado nem massa de dados de exemplo — o banco começa vazio. Para testar, cadastre um usuário pela própria tela de **Criar conta** (`/cadastro`) ou diretamente pela API:

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teste","email":"teste@teste.com","senha":"senha123"}'
```

A resposta já traz o token JWT (cadastro autentica automaticamente, sem precisar de um segundo login):

```json
{"token":"eyJhbGciOiJIUzUxMiJ9..."}
```

## Endpoints

Todas as rotas exigem `Authorization: Bearer <token>`, exceto `/auth/**`.

### Autenticação

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/auth/register` | Cadastra um usuário e já devolve o token |
| `POST` | `/auth/login` | Autentica e devolve o token |

**Corpo de `/auth/register`:**
```json
{ "nome": "string", "email": "string", "senha": "string (mín. 6 caracteres)" }
```

**Corpo de `/auth/login`:**
```json
{ "email": "string", "senha": "string" }
```

### Pedidos

Compartilhados entre todos os administradores autenticados — não há escopo por usuário; qualquer um que estiver logado vê e gerencia todos os pedidos do sistema. O `cliente` é um campo de texto livre informado na criação do pedido, sem relação com o usuário autenticado.

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/pedidos` | Cria um pedido, status inicial `RECEBIDO` |
| `GET` | `/pedidos` | Lista todos os pedidos do sistema |
| `GET` | `/pedidos/{id}` | Detalha um pedido específico |
| `PUT` | `/pedidos/{id}/status` | Atualiza o status do pedido |

**Corpo de `POST /pedidos`:**
```json
{
  "cliente": "string",
  "enderecoEntrega": "string",
  "itens": [
    { "nome": "string", "quantidade": 1, "preco": 10.50 }
  ]
}
```

**Corpo de `PUT /pedidos/{id}/status`:**
```json
{ "status": "RECEBIDO | EM_PREPARO | SAIU_PARA_ENTREGA | ENTREGUE | CANCELADO" }
```

### Formato de erro

Todo erro (400, 401, 404, 409, 500) segue o mesmo formato:

```json
{
  "timestamp": "2026-07-30T21:21:05.65",
  "status": 400,
  "mensagem": "Dados invalidos",
  "path": "/pedidos",
  "campos": { "enderecoEntrega": "não deve estar em branco" }
}
```

O campo `campos` só aparece em erros de validação de formulário.

## Escopo

Este projeto foi desenvolvido como um desafio técnico, dimensionado para poucas horas de trabalho, e cobre integralmente o que foi pedido: autenticação, CRUD de pedidos com os status definidos, persistência em SQLite e o frontend em React.

Itens como cadastro de clientes reaproveitável, perfis de acesso entre administradores, máquina de estados na transição de status, paginação, testes automatizados, CI, refresh token e edição de pedido após criado ficaram fora do escopo. São decisões deliberadas para este teste — não lacunas por desconhecimento — e seriam naturalmente endereçadas em um cenário real de produção, com prazo e contexto de negócio para justificá-las.
