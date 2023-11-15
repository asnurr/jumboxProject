package com.aynlss.jumboxproject.data.model.response

data class GetProductsResponse(
    val products: List<Product>?
) : BaseResponse()

