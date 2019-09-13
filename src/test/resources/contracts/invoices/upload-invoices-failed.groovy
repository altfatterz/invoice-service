import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.ServerDslProperty

Contract.make {
    description "Failed invoice upload"

    request {
        method 'POST'
        urlPath '/invoices'
        headers {
            contentType('multipart/form-data')
        }
        multipart(
                [
                        files: named(
                                // name of the file
                                name: $(c(regex(nonEmpty())), p('invoice.jpg')),
                                // content of the file
                                content: $(c(regex(nonEmpty())), new ServerDslProperty(file("invoice.jpg"))),
                                // content type for the part
                                contentType: $(c(regex(nonEmpty())), p('image/jpg'))
                        )
                ]
        )

    }
    response {
        status BAD_REQUEST()
    }
}
