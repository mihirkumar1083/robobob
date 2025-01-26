# RoboBob App

## Description

RoboBob is a children's mathematics robot app that can respond to predefined questions and evaluate basic arithmetic expressions. The app supports two types of questions:
1. Basic questions about RoboBob (a limited predefined set).
2. Arithmetic expressions.

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/mihirkumar1083/robobob.git
cd RoboBob

mvn clean install
mvn test
mvn spring-boot:run
By default, the app will run on http://localhost:8080.

Testing : 

curl -X POST "http://localhost:8080/api/questions" -H "Content-Type: text/plain" -d "2+2"
Response : 
{
  "answer": "4"
}


curl -X POST "http://localhost:8080/api/questions" -H "Content-Type: text/plain" -d "What is your name"
Response : 
{
  "answer": "RoboBob"
}

