### Examples 

Invoice upload test:

```bash
http -v -f :8080/invoices files@./samples/invoice1.pdf files@./samples/invoice2.pdf

POST /invoices HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 16520
Content-Type: multipart/form-data; boundary=8a19fde949172660a36f7bf79285027c
Host: localhost:8080
User-Agent: HTTPie/1.0.2



+-----------------------------------------+
| NOTE: binary data not shown in terminal |
+-----------------------------------------+

HTTP/1.1 200
Content-Length: 0
Date: Fri, 13 Sep 2019 13:03:17 GMT
```

Generate the tests

```bash
$ mvn clean spring-cloud-contract:generateTests
```

Start up the stubrunner via [`Spring Boot Cloud CLI`](https://cloud.spring.io/spring-cloud-cli/reference/html/)

```bash
$ sdk install springboot 2.1.8.RELEASE
$ spring install org.springframework.cloud:spring-cloud-cli:2.1.0.RELEASE
$ spring cloud stubrunner  
```

```bash
$ http -v -f :9876/invoices files@./samples/invoice1.pdf files@./samples/invoice2.pdf
```


Difference between `@RequestParam` vs `@RequestPart`:
https://stackoverflow.com/questions/38156646/using-requestparam-for-multipartfile-is-a-right-way
