Projeto DSCommerce

1. Premissas
Deseja-se fazer um sistema para ser utilizado em cursos da Devsuperior no processo de
aprendizado dos alunos. Para isto, a concepção do sistema partiu das seguintes
premissas:
- Deve ser um sistema que possua um modelo de domínio relativamente simples,
porém abrangente, ou seja, que explore vários tipos de relacionamentos entre as
entidades de negócio (muitos-para-um, muitos-para-muitos, etc.).
- O sistema deve possibilitar a aplicação de vários conhecimentos importantes das
disciplinas de fundamentos.
- O sistema deve conter as principais funcionalidades que se espera de um
profissional iniciante deve saber construir, tais como telas de cadastro e fluxos de
caso de uso.

2. Visão geral do sistema
O sistema deve manter um cadastro de usuário, produtos e suas categorias. Cada
usuário possui nome, email, telefone, data de nascimento e uma senha de acesso. Os
dados dos produtos são: nome, descrição, preço e imagem. O sistema deve apresentar
um catálogo de produtos, os quais podem ser filtrados pelo nome do produto. A partir
desse catálogo, o usuário pode selecionar um produto para ver seus detalhes e para
decidir se o adiciona a um carrinho de compras. O usuário pode incluir e remover itens
do carrinho de compra, bem como alterar as quantidades de cada item. Uma vez que o
usuário decida encerrar o pedido, o pedido deve então ser salvo no sistema com o status
de "aguardando pagamento". Os dados de um pedido são: instante em que ele foi salvo,
status, e uma lista de itens, onde cada item se refere a um produto e sua quantidade no
pedido. O status de um pedido pode ser: aguardando pagamento, pago, enviado,
entregue e cancelado. Quando o usuário paga por um pedido, o instante do pagamento
deve ser registrado. Os usuários do sistema podem ser clientes ou administradores,
sendo que todo usuário cadastrado por padrão é cliente. Usuários não identificados
podem se cadastrar no sistema, navegar no catálogo de produtos e no carrinho de
compras. Clientes podem atualizar seu cadastro no sistema, registrar pedidos e visualizar
seus próprios pedidos. Usuários administradores tem acesso à área administrativa onde
pode acessar os cadastros de usuários, produtos e categorias.

3. Protótipos de tela:
https://www.figma.com/file/ZrGNVNG0kZL6txDv4G8P6s/DSCommerce

4. Modelo conceitual
Este é o modelo conceitual do sistema DSCommerce. Considerações:
- Cada item de pedido (OrderItem) corresponde a um produto no pedido, com uma
quantidade. Sendo que o preço também é armazenado no item de pedido por
questões de histórico (se o preço do produto mudar no futuro, o preço do item de
pedido continua registrado com o preço real que foi vendido na época).
- Um usuário pode ter um ou mais "roles", que são os perfis de acesso deste usuário
no sistema (client, admin).

![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/1da9a5a0-624f-4b76-ab80-53480ba04d86)

5. Casos de uso (visão geral)
O escopo funcional do sistema consiste nos seguintes casos de uso:

![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/31011da7-7d9e-4868-a110-64bf3b19ea9a)
![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/7b5b2531-4fa3-4d65-8bdd-60b3a96cafa6)

![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/2bf9197e-6aab-4e0c-813d-94cf15a4bd54)

![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/6f23cc74-c821-4549-ba1a-4bd5d7991e37)

![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/c67932be-4e1a-4a28-825c-5fde57b13677)


![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/bb774796-35d8-4cb6-8455-82f2fe1dd547)


![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/be71c745-f247-4b7a-81a3-6d6a34215ac6)


![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/d7c1989e-473f-4db7-90b9-1500893522f7)


![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/079b4c3b-cbec-46b6-a930-aafaf869992a)

![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/512c92c9-71b9-4cf3-81bd-512d3810e2b9)

![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/a2366a3d-b270-426d-a580-cc73a99c6baa)

![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/94163148-8147-4982-a8c5-6efbc8aec55f)

![image](https://github.com/JonasRF/DSCommerce-Backend/assets/77034798/f5ba6b05-06b3-45de-a494-9fe15ae41c72)


