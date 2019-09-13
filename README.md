Uploading files using a demo `invoice-service` 

### Upload success 

#### Invoice upload test with `pdf` files

```bash
$ http -v -f :8080/invoices files@./samples/invoice1.pdf files@./samples/invoice2.pdf

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

#### Invoice upload test with `images` files

```bash
$ http -v -f :8080/invoices files@./samples/invoice3.jpg

POST /invoices HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 51260
Content-Type: multipart/form-data; boundary=702c2e570f95b7752e9022ea43be8326
Host: localhost:8080
User-Agent: HTTPie/1.0.2



+-----------------------------------------+
| NOTE: binary data not shown in terminal |
+-----------------------------------------+

HTTP/1.1 200
Content-Length: 0
Date: Fri, 13 Sep 2019 20:10:40 GMT
```

### Upload failed

#### Not supported mime type

```bash
$ http -v -f :8080/invoices files@./samples/invoice4.txt

POST /invoices HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 190
Content-Type: multipart/form-data; boundary=45b35d67bd487b6f302672969191e54b
Host: localhost:8080
User-Agent: HTTPie/1.0.2

--45b35d67bd487b6f302672969191e54b
Content-Disposition: form-data; name="files"; filename="invoice4.txt"
Content-Type: text/plain

payment is late
--45b35d67bd487b6f302672969191e54b--

HTTP/1.1 400
Connection: close
Content-Type: application/json;charset=UTF-8
Date: Fri, 13 Sep 2019 20:12:02 GMT
Transfer-Encoding: chunked

{
    "error": "Bad Request",
    "message": "only [image/jpeg, image/jpg, application/pdf] are allowed",
    "path": "/invoices",
    "status": 400,
    "timestamp": "2019-09-13T20:12:02.290+0000"
}
```

#### Not valid content

```bash
⇒  http -v -f :8080/invoices files@./samples/invoice6.jpg

POST /invoices HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 8274
Content-Type: multipart/form-data; boundary=febfd9089a44ea85d2d4f5c5fb1d10d8
Host: localhost:8080
User-Agent: HTTPie/1.0.2



+-----------------------------------------+
| NOTE: binary data not shown in terminal |
+-----------------------------------------+

HTTP/1.1 400
Connection: close
Content-Type: application/json;charset=UTF-8
Date: Fri, 13 Sep 2019 20:13:37 GMT
Transfer-Encoding: chunked

{
    "error": "Bad Request",
    "message": "the file content is not in sync with the file extension",
    "path": "/invoices",
    "status": 400,
    "timestamp": "2019-09-13T20:13:37.255+0000"
}
```

### Content detection using [Apache Tika](https://tika.apache.org/)

```xml
<dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-core</artifactId>
    <version>1.22</version>
</dependency>
``` 

Tika detects the media type of the given document. The type detection is based on the content of the given document stream.

```java
Tika tika = new Tika();
String detectedContentType = tika.detect(multipartFile.getInputStream());
```

### Control upload size

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 1MB
      max-request-size: 10MB
```

### Spring Cloud Contract 

Generate the tests

```bash
$ mvn clean spring-cloud-contract:generateTests
```

The `InvoicesTest` is generated

```java
public class InvoicesTest extends ContractBaseClass {

	@Test
	public void validate_upload_invoices_failed() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "multipart/form-data")
					.multiPart("files", "invoice.jpg", new byte[] {37, 80, 68, 70, 45}, "image/jpg");

		// when:
			ResponseOptions response = given().spec(request)
					.post("/invoices");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
	}

	@Test
	public void validate_upload_invoices_success() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "multipart/form-data")
					.multiPart("files", "invoice.pdf", new byte[] {37, 80, 68, 70, 45, 49, 46}, "application/pdf");

		// when:
			ResponseOptions response = given().spec(request)
					.post("/invoices");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
	}

}
```

### Stubrunner
Start up the stubrunner via [`Spring Boot Cloud CLI`](https://cloud.spring.io/spring-cloud-cli/reference/html/)

```bash
$ sdk install springboot 2.1.8.RELEASE
$ spring install org.springframework.cloud:spring-cloud-cli:2.1.0.RELEASE
$ spring cloud stubrunner  
```

The command is using the `stubrunner.yml`

```yaml
stubrunner:
  stubsMode: LOCAL
  ids:
  - com.example:invoice-service:+:9876
```

To see the registered stubs:

```bash
$ http :8750/stubs

HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Date: Fri, 13 Sep 2019 20:00:02 GMT
Transfer-Encoding: chunked

{
    "com.example:invoice-service:0.0.1-SNAPSHOT:stubs": 9876
}
```

The `invoice-service` stub is running on the 9876 port 

```bash
$ http -v -f :9876/invoices files@./samples/invoice1.pdf files@./samples/invoice2.pdf

POST /invoices HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 16520
Content-Type: multipart/form-data; boundary=ff4bb10bb5deb38ab89910a95bdd4910
Host: localhost:9876
User-Agent: HTTPie/1.0.2



+-----------------------------------------+
| NOTE: binary data not shown in terminal |
+-----------------------------------------+

HTTP/1.1 200 OK
Content-Encoding: gzip
Matched-Stub-Id: 2809a14f-34bb-41db-9fd2-f16b4fefcfad
Server: Jetty(9.2.z-SNAPSHOT)
Transfer-Encoding: chunked
Vary: Accept-Encoding, User-Agent
```


