# Gerenciamento de endereços

A aplicação contém web-services para busca de CEP e gerenciamento de endereços, que permite incluir, 
consultar, atualizar e deletar um endereço. Este projeto possui web-services REST que consomem
e produzem respostas em JSON. A aplicação foi desenvolvida utilizando Java 1.8 e Gradle para *build* do projeto.

## Pré-requisitos

- Java 1.8 (de preferência 1.8.0_111)
- Definição da variável de ambiente *JAVA_HOME*, que aponta para a instalação do JAVA

## Configuração e Build

As seguintes tecnologias foram utilizadas:

- Java 8;
- SpringBoot;
- RESTful Web-Services;
- Hibernate Validator;
- Spring Web MVC;
- SpringData with JPA;
- HSQLDB no modo em memória que foi populado com informações de CEP mocadas (arquivo data.sql no *classpath*);
- Jetty;
- Logback para logs;
- JSON;
- Google Guava para manter invariantes dos métodos (pré-condições sobre parâmetros que não podem ser nulos);
- JUnit 4, Mockito e Spring-test para testes unitários e de integração;
- Hamcrest matchers para tornar os *asserts* nos testes mais elegantes;
- jsonpath para fazer *asserts* do JSON retornado pelos web-services REST;
- Gradle para *build* do projeto

A aplicação deve ser construído na sua raiz com o seguinte comando:

***./gradlew clean build*** (em sistemas Unix) e ***./gradlew.bat clean build*** (Windows)

Esse comando utiliza o Gradle *wrapper* para baixar a distribuição do Gradle apropriada.

## Iniciando a aplicação

A aplicação deve ser iniciada na raiz do projeto address-management com o seguinte comando Gradle: ***./gradlew bootRun***

Esse comando realiza as seguintes tarefas: 

- *build* da aplicação, execução dos testes unitários e integrados;
- *Deploy* da aplicação no Jetty na porta 8080 se todos os testes unitários e integrados forem executados com sucesso;

Os logs da aplicação se encontram em */tmp/logs/address-management/address-management.log*

## Testes unitários e integrados

Os testes unitários podem ser executados individualmente através do seguinte comando Gradle: 

***./gradlew test*** 

Os testes integrados podem ser executados individualmente através do comando Gradle:
 
***./gradlew integrationTest*** 

## Arquitetura e padrões

É uma aplicação que utiliza como base o framework SpringBoot. 

A aplicação está modelada com os seguintes padrões e separação de camadas:

- *MVC*: *Endpoint* (*Controllers* REST), *Service*, *Repository* e *Domain*.

- *Endpoint (Controller)* : São responsáveis por receber a requisição http, consumir e produzir
 JSON e enviar a requisição para algum serviço para processamento. Exemplo: classe CepSearchEndpoint
 que é o serviço REST responsável por buscar o CEP.

- *Service*: classes nessa camada interagem com outros serviços, com a camada de repositório e domínio. 
Exemplo: CepSearchService que interage com o repositório de CEP para buscar o CEP.

- *Repository*: classes que interagem com o banco de dados e são responsáveis pelas operações de CRUD
nas entidades JPA. Exemplo: CepRepository que busca o CEP na base.

- *Domain*: classes que representam entidades que são criadas, atualizadas, pesquisadas e removidas da base de dados.
Exemplo: classes *Cep* e *Address*

- Padrão de injeção de dependências: Aplicado com o uso do *framework* Spring, 
facilitando código testável e reutilizável.

Os dados de CEP são mocados e estão no arquivo data.sql, utilizado pelo banco em memória
HSQLDB que carrega o arquivo quando a aplicação inicia. Dados de CEP não são alterados, apenas consultados.

Os dados de endereço são incluídos, atualizados, pesquisados e removidos do HSQLDB em memória, 
não persistindo entre uma execução e outra da aplicação.

As classes de nome *Cep* e *Address* são as entidades do sistema. O endereço possui
número e complemento. O endereço também possui um CEP, portanto, a classe *Address* 
possui uma referência para a classe *Cep*.

Um relacionamento unidirecional de *Address* para *Cep* foi criado, já que o CEP não possui número e complemento.

Uma anotação para validar o CEP foi criada para ser utilizada pelo *HibernateValidator* para validar as requisições aos web-services REST
com CEP mal formado.

A classe *ReplaceRightDigitToZeroCepGenerator* é responsável por implementar a regra de substituição de um dígito da direita
para a esquerda do CEP por zero. Ela é utilizada pelo serviço de busca de CEP *CepSearchServiceImpl* durante a busca de CEP.

Esse serviço é utilizado também pelo serviço de busca de endereços, *AddressManagementServiceImpl* para validar e buscar
a existência do CEP durante as operações de CRUD de endereço.

A entidade *Cep* não é atualizada, apenas a entidade *Address* que possui um *Cep*.

As dependências entre as classes são injetadas via construtor (injeção via construtor).

Todos os serviços e repositórios têm interfaces que facilitam a injeção de dependências e a 
utilização de mocks durante os testes unitários. 

As classes são desacopladas de uma implementação específica e interagem apenas com a interface, evidenciando
como elas colaboram umas com as outras através da interface pública exposta por cada uma delas.

## Serviços REST

*GET* **http://localhost:8080/cep/{cep}**

Respostas

● 200: Um CEP no formato JSON

Exemplo: http://localhost:8080/cep/09371616

```json
{
  "id": 4,
  "value": "09371616",
  "street": "Rua Deonildo Nincão",
  "district": "Parque São Vicente",
  "city": "Mauá",
  "estate": "SP"
}
```

● 200: Um CEP no formato JSON. 

Este caso utiliza a substituição de um dígito da direita para esquerda por zero até que o CEP seja encontrado.

Exemplo: http://localhost:8080/cep/61903123

```json
{
  "id": 6,
  "value": "61903000",
  "street": "Rua Manoel Costa Barros",
  "district": "Antônio Justa",
  "city": "Maracanaú",
  "estate": "CE"
}
```

● 400: Cep inválido

Exemplo: http://localhost:8080/cep/123a4567

```json
{
  "errors": [
    {
      "error": "CEP inválido"
    }
  ]
}
```

● 400: CEP não encontrado

Exemplo: http://localhost:8080/cep/12345678

```json
{
  "errors": [
    {
      "error": "Cep=[12345678] nao encontrado"
    }
  ]
}
```

● 500: Erro interno

```json
{
  "errors": [
    {
      "error": "Erro inesperado. Tente novamente."
    }
  ]
}
```

*POST* **http://localhost:8080/address**

Aceita application/json;charset=UTF-8

Recebe e inclui um endereço na base de dados

```json
{
  "cep": {
    "value": "78715155",
    "street": "Rua Natal",
    "district": "Jardim Tropical",
    "city": "Rondonópolis",
    "estate": "MT"
  },
  "complement": "Perto do Shopping novo",
  "number": "79"
}
```

Respostas

● 201: JSON do endereço criado

```json
{
  "id": 1,
  "cep": {
    "id": 2,
    "value": "78715155",
    "street": "Rua Natal",
    "district": "Jardim Tropical",
    "city": "Rondonópolis",
    "estate": "MT"
  },
  "complement": "Perto do Shopping novo",
  "number": "79"
}
```

*Header Location* contém a URL do endereço criado: http://localhost:8080/address/1

● 200: Caso o endereço com mesmo CEP, complemento e número exista, ele é retornado e um outro igual
não é criado

```json
{
  "id": 1,
  "cep": {
    "id": 2,
    "value": "78715155",
    "street": "Rua Natal",
    "district": "Jardim Tropical",
    "city": "Rondonópolis",
    "estate": "MT"
  },
  "complement": "Perto do Shopping novo",
  "number": "79"
}
```

● 400: Caso um ou mais dos atributos obrigatórios não seja fornecido, mensagens de erro são
retornadas

```json
{
  "cep": {
    "value": "78715155",
    "street": "Rua Natal",
    "district": "Jardim Tropical"
  },
  "complement": "Perto do Shopping novo",
  "number": "79"
}
```

Resposta: 

```json
{
  "errors": [
    { 
      "error": "Cidade inválida"
    },
    {
      "error": "Estado inválido"
    }
  ]
}
```

● 400: Caso a informação de CEP seja inválida, um erro é retornado

```json
{
  "cep": {
    "value": "7871-5155",
    "street": "Rua Natal",
    "district": "Jardim Tropical",
    "city": "Rondonópolis",
    "estate": "MT"
  },
  "complement": "Perto do Shopping novo",
  "number": "79"
}
```

Resposta:

```json
{
  "errors": [
    {
      "error": "CEP inválido"
    }
  ]
}
```

● 400: Caso a informação de CEP não exista na base de dados, um erro também é retornado

```json
{
  "cep": {
    "value": "12345678",
    "street": "Rua Natal",
    "district": "Jardim Tropical",
    "city": "Rondonópolis",
    "estate": "MT"
  },
  "complement": "Perto do Shopping novo",
  "number": "79"
}
```

Resposta: 

```json
{
  "errors": [
    {
      "error": "Cep=[12345678] nao encontrado"
    }
  ]
}
```

● 500: Erro interno

```json
{
  "errors": [
    {
      "error": "Erro inesperado. Tente novamente."
    }
  ]
}
```

*POST* **http://localhost:8080/address/{id}**

Parâmetro

● id: Id do endereço, obrigatório

Aceita application/json;charset=UTF-8

Recebe e atualiza um endereço (seu CEP, número ou complemento) na base de dados

Exemplo: http://localhost:8080/address/1

```json
{
  "cep": {
    "value": "68909608",
    "street": "Alameda Colibrir",
    "district": "Boné Azul",
    "city": "Macapá",
    "estate": "MT"
  },
  "complement": "Perto da praça",
  "number": "34"
}
```

Resposta:

● 200: JSON do endereço atualizado

```json
{
  "id": 1,
  "cep": {
    "id": 3,
    "value": "68909608",
    "street": "Alameda Colibrir",
    "district": "Boné Azul",
    "city": "Macapá",
    "estate": "AP"
  },
  "complement": "Perto da praça",
  "number": "34"
}
```

● 400: Caso o endereço a ser atualizado não exista na base de dados, um erro é retornado

Exemplo: http://localhost:8080/address/10

```json
{
  "cep": {
    "value": "68909608",
    "street": "Alameda Colibrir",
    "district": "Boné Azul",
    "city": "Macapá",
    "estate": "MT"
  },
  "complement": "Perto da praça",
  "number": "34"
}
```

Resposta: 

```json
{
  "errors": [
    {
      "error": "Endereco com id=[2] nao encontrado"
    }
  ]
}
```

● 500: Erro interno

```json
{
  "errors": [
    {
      "error": "Erro inesperado. Tente novamente."
    }
  ]
}
```

Na atualização do endereço, os mesmos erros de validação do CEP que ocorrem no processo de criação
do endereço também são retornados.

*GET* **http://localhost:8080/address/{id}**

Parâmetro

● id: Id do endereço, obrigatório

Respostas

● 200: Endereço no formato JSON

Exemplo: http://localhost:8080/address/1

```json
{
  "id": 1,
  "cep": {
    "id": 3,
    "value": "68909608",
    "street": "Alameda Colibrir",
    "district": "Boné Azul",
    "city": "Macapá",
    "estate": "AP"
  },
  "complement": "Perto da praça",
  "number": "34"
}
```

● 400: Caso o endereço não exista na base de dados, um erro é retornado

Exemplo: http://localhost:8080/address/20

Resposta: 

```json
{
  "errors": [
    {
      "error": "Endereco com id=[2] nao encontrado"
    }
  ]
}
```

● 500: Erro interno

```json
{
  "errors": [
    {
      "error": "Erro inesperado. Tente novamente."
    }
  ]
}
```

*DELETE* **http://localhost:8080/address/{id}**

Parâmetro

● id: Id do endereço, obrigatório

Respostas

● 200: Endereço removido da base no formato JSON

Exemplo: http://localhost:8080/address/1

```json
{
  "id": 1,
  "cep": {
    "id": 3,
    "value": "68909608",
    "street": "Alameda Colibrir",
    "district": "Boné Azul",
    "city": "Macapá",
    "estate": "AP"
  },
  "complement": "Perto da praça",
  "number": "34"
}
```

● 400: Caso o endereço não exista na base de dados para ser removido, um erro é retornado

Exemplo: http://localhost:8080/address/25

Resposta: 

```json
{
  "errors": [
    {
      "error": "Endereco com id=[25] nao encontrado"
    }
  ]
}
```