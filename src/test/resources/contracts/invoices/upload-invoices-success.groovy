import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.ServerDslProperty

Contract.make {
    description "Successful invoice upload"

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
                                name: $(c(regex(nonEmpty())), p('invoice.pdf')),
                                // content of the file
                                content: $(c(regex(nonEmpty())), new ServerDslProperty(file("invoice.pdf"))),
                                // content type for the part
                                contentType: $(c(regex(nonEmpty())), p('application/pdf'))
                        )
                ]
        )

    }
    response {
        status OK()
    }
}
