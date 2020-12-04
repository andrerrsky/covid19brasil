# App COVID 19 Brasil
App desenvolvido em JAVA para Android com dados sobre COVID-19 no Brasil atualizados em tempo real através da API Brasil.IO.

## Contexto
A pandemia de COVID-19, também conhecida como pandemia de coronavírus, é uma pandemia em curso de COVID-19, uma doença respiratória aguda causada pelo coronavírus da síndrome respiratória aguda grave 2 (SARS-CoV-2). A doença foi identificada pela primeira vez em Wuhan, na província de Hubei, República Popular da China, em 1 de dezembro de 2019, mas o primeiro caso foi reportado em 31 de dezembro do mesmo ano. Acredita-se que o vírus tenha uma origem zoonótica, porque os primeiros casos confirmados tinham principalmente ligações ao Mercado Atacadista de Frutos do Mar de Huanan, que também vendia animais vivos. Em 11 de março de 2020, a Organização Mundial da Saúde declarou o surto uma pandemia. Até 4 de dezembro de 2020, pelo menos 65 132 317 casos da doença foram confirmados em pelo menos 191 países e territórios, com cerca de 1 504 443 fatalidades reportadas e 41 862 236 pessoas curadas.
*Fonte: https://pt.wikipedia.org/wiki/Pandemia_de_COVID-19*

## Motivação
Nesse ano a Lei de Acesso à Informação completou 8 anos e ainda temos problemas graves na divulgação de dados abertos. Os dados sobre o coronavírus disponibilizados pelo Ministério da Saúde não são suficientes para que possamos agir localmente com eficácia, pois: 

- **O processo de atualização é lento pouco frequente;**
- **O site sai do ar frequentemente;**
- **Os dados não estão estruturados.**

*Fonte: https://blog.brasil.io/2020/03/23/dados-coronavirus-por-municipio-mais-atualizados/* 

A partir disso foi criada a API e dataset do Brasil.IO com boletins epidemiológicos da COVID-19, eles catalogam manualmente os dados presentes em centenas de boletins epidemiológicos das secretarias de saúde estaduais incluindo o histórico, um trabalho incrível não? ♥.

## O Projeto
Com o objetivo de disponibilizar um exemplo de consumo da API criada por eles para Android em JAVA (nativo) desenvolvi esse app, sinta-se a vontade para estudar e utilizar, espero que ajude.

O consumo da API é realizado através da biblioteca Volley com.android.volley:volley:1.1.1 (https://developer.android.com/training/volley?hl=pt-br);

## Instruções e outras informações
- Basta clonar e abrir no Android Studio 4.0.1 ou superior;
- compileSdkVersion 30;
- minSdkVersion 16;
- targetSdkVersion 30;
- buildToolsVersion "29.0.3";
- androidx.appcompat;
- build:gradle:3.6.1.

### Não recomendo a publicação desse app na Google Play pois:

De acordo com a Seção 8.3 do Contrato de distribuição do desenvolvedor e a política de execução (Google Play), os aplicativos que fazem referência a COVID - 19 ou termos relacionados, de qualquer forma, só serão aprovados para distribuição no Google Play se forem publicados, comissionados ou autorizados por entidades governamentais oficiais ou organizações de saúde pública. O Google reserva-se o direito de remover aplicativos do Google Play com base em uma série de fatores, incluindo um alto risco de abuso.

### Obs: Atualmente estou sem tempo para manter o código atualizado e realizar as devidas manutenções, pretendo realizar updates em breve.

## Dados
- **Fonte: Secretarias de Saúde das Unidades Federativas, dados tratados por Álvaro Justen e equipe de voluntários [Brasil.IO](https://brasil.io/)**
- **Brasil.IO: boletins epidemiológicos da COVID-19 por município por dia, disponível em: https://brasil.io/dataset/covid19/**
- **Fonte do Ícone: https://www.flaticon.com/**

