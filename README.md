# Taskboard Application

Welcome to the Taskboard Project— which stands as the backend for the
[client side project](https://github.com/pushpalroy/JetTaskBoardKMP) written with Jetbrains KMP.
This is a simple P0 clone that emulates a kanban board with boards, buckets and cards.

Access Swagger link using the endpoint- /webjars/swagger-ui/index.html

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
- Keyset Pagination
- JWT based Authentication
- Testing
    - Unit Testing Controllers
    - Integration Testing Repositories
    - Concurrency Testing Card Update

### 📁 DB Schema Design

![DB design](art/db_schema.png)

### 🤔 Why Server Sent Events?

Server-Sent Events (SSE) enables a unidirectional, real-time communication channel between a server and a client. With
SSE, the server can initiate a continuous stream of updates or events to the client over a single HTTP connection.
SSE was considered over websockets since it provides an asynchronous unidirectional server push. Some links that
informed me about this was from
a [LinkedIn blog](https://engineering.linkedin.com/blog/2016/10/instant-messaging-at-linkedin--scaling-to-hundreds-of-thousands-)
and [stackoverflow post](https://stackoverflow.com/questions/5195452/websockets-vs-server-sent-events-eventsource).
Also, a performance comparison on [sse and websocket](https://www.timeplus.com/post/websocket-vs-sse).

### 👀 Why Optimistic Lock?

Locks are necessary to prevent concurrent row changes in the database. This mechanism is used to prevent concurrent card
updates in this project. Some videos on the topic are [Link 1](https://youtu.be/I8IlO0hCSgY)
and [Link 2](https://youtu.be/H_zJ81I_D5E).

Another solution to prevent concurrent transactions is to
use [Serializable Transactions Isolation](https://www.postgresql.org/docs/current/transaction-iso.html#XACT-SERIALIZABLE)
instead of locking rows. There are several other isolation to prevent bad data (i.e. dirty/repeatable reads) but using
serializable transactions reduces the effect of webflux and suspend transactions allowing only sequential data access.

### 📑 Pagination

Most common way of implementing pagination is to use offset-based pagination. This involves specifying an offset (the
number of records to skip) and a limit (the maximum number of records to return) in a query. This can be problematic
when
records can be shifted leading to records being skipped/truncated or duplicated. This can also become slower as the
number of records increases. This happens as the database still needs to read up to the offset number of rows to
determine where to start selecting data. Another [link](https://youtu.be/WDJRRNCGIRs) on why not to use offset
pagination.
In this project, I'm using [Keyset Pagination](https://use-the-index-luke.com/sql/partial-results/fetch-next-page)
instead.

### 🏗️ How to set up the project.

1. Create a Database with the name "kanbanDB".
2. Run flyway gradle migrate task and make sure that the tables are created in the DB.
3. Run generate jOOQ task to find the generated classes in build/kotlin folder.

### 💬 Want to discuss?

Have any questions, doubts or want to present your opinions, views? You're always welcome.

### 🔭 Find this project useful?

Support it by clicking the ⭐️ button on the upper right of this page.

### To improve

- Implement refresh token functionality.
- Use Webflux Test instead of unit testing controllers.

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