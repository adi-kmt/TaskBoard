# Taskboard Application

Welcome to the Taskboard Project— which stands as the backend for the
[client side project](https://github.com/pushpalroy/JetTaskBoardKMP) written with KMP.
This is a simple P0 clone that emulates a kanban board with boards, buckets and cards.

### 👨‍💻 Tech stack

| Tools             |                         Link                         |
|:------------------|:----------------------------------------------------:|
| 🤖  Language      |           [Kotlin](https://kotlinlang.org)           |
| 💚  Framework     | [SpringBoot](https://spring.io/projects/spring-boot) |
| 📁  DB Access     |            [jOOQ](https://www.jooq.org/)             |
| 📼  DB Versioning |           [flyway](https://flywaydb.org/)            |
| 🔍  Testing       |              [mockk](https://mockk.io/)              |

### 🧳 Features

- Row based Optimistic Lock
- Server Sent Events
- JWT based Authentication
- Testing
    - Unit Testing Controllers
    - Integration Testing Repositories
    - Concurrency Testing Card Update

### 📁 DB Schema Design

### 🤔 Why Server Sent Events?

Server-Sent Events (SSE) enables a unidirectional, real-time communication channel between a server and a client. With
SSE, the server can initiate a continuous stream of updates or events to the client over a single HTTP connection.
SSE was considered over websockets since it provides an asynchronous unidirectional server push. Some links that
informed me about this was from
a [LinkedIn blog](https://engineering.linkedin.com/blog/2016/10/instant-messaging-at-linkedin--scaling-to-hundreds-of-thousands-)
and [stackoverflow post](https://stackoverflow.com/questions/5195452/websockets-vs-server-sent-events-eventsource).
Also,
a performance comparison on [sse and websocket](https://www.timeplus.com/post/websocket-vs-sse).

### 👀 Why Optimistic Lock?

Locks are necessary to prevent concurrent row changes in the database. This mechanism is used to prevent concurrent card
updates in this project. Some videos on the topic are [Link 1](https://youtu.be/I8IlO0hCSgY)
and [Link 2](https://youtu.be/H_zJ81I_D5E).

### 🏗️ How to set up the project.

1. Create a Database with the name "kanbanDB".
2. Run flyway gradle migrate task and make sure that the tables are created in the DB.
3. Run generate jOOQ task to find the generated classes in build/kotlin folder.

### 💬 Want to discuss?

Have any questions, doubts or want to present your opinions, views? You're always welcome.

### 🔭 Find this project useful?

Support it by clicking the ⭐️ button on the upper right of this page.

### 📚 License

```
MIT License

Copyright (c) 2023 Adithya Kamath

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and 
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial 
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO 
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```