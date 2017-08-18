package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
	request {
		method 'GET'
		url '/value'
	}
	response {
		status 200
		body (
			  value: 'Producer Entity Value Not Set'
			)
		headers {
			contentType(applicationJson())
		}
	}
}