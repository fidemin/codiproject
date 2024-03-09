# codiproject

## Environment

아래 환경 기준으로 테스트 되었다.

- java version: openjdk 21
- gradle version: 8.5

## Build

```bash
gradle build
```

## Run server

```bash
java -jar build/libs/codi-0.0.1.jar
```

localhost:8080 포트로 접근할 수 있다.

## API

### 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회

- uri: `/api/codi/products/minPriceByCategory`
- method: `GET`
- response
    - 성공
        - status code: `200`
        - response body example

          ```json
          {
              "success": true,
              "errorMessage": null,
              "data": {
                  "minPriceByCategory": [
                      {
                          "brandName": "A",
                          "category": "SNEAKERS",
                          "price": 9000
                      },
                      {
                          "brandName": "A",
                          "category": "BAG",
                          "price": 2000
                      },
                      {
                          "brandName": "C",
                          "category": "TOP",
                          "price": 10000
                      },
                      {
                          "brandName": "D",
                          "category": "PANTS",
                          "price": 3000
                      },
                      {
                          "brandName": "D",
                          "category": "HAT",
                          "price": 1500
                      },
                      {
                          "brandName": "E",
                          "category": "OUTERWARE",
                          "price": 5000
                      },
                      {
                          "brandName": "F",
                          "category": "ACCESSORY",
                          "price": 1900
                      },
                      {
                          "brandName": "I",
                          "category": "SOCKS",
                          "price": 1700
                      }
                  ],
                  "totalPrice": 34100
              }
          }
          ```

### 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회

- uri: /api/codi/brands/withMinPrice
- method: `GET`
- response
    - 성공
        - status code: `200`
        - response body example

            ```json
            {
                "success": true,
                "errorMessage": null,
                "data": {
                    "brandId": 4,
                    "brandName": "D",
                    "categories": [
                        {
                            "category": "TOP",
                            "price": 10100
                        },
                        {
                            "category": "OUTERWARE",
                            "price": 5100
                        },
                        {
                            "category": "PANTS",
                            "price": 3000
                        },
                        {
                            "category": "SNEAKERS",
                            "price": 9500
                        },
                        {
                            "category": "BAG",
                            "price": 2500
                        },
                        {
                            "category": "HAT",
                            "price": 1500
                        },
                        {
                            "category": "SOCKS",
                            "price": 2400
                        },
                        {
                            "category": "ACCESSORY",
                            "price": 2000
                        }
                    ],
                    "totalPrice": 36100
                }
            }

            ```
    - brand가 존재하지 않음
        - status code: `404`
        - response body example

            ```json
            {
                "success": false,
                "errorMessage": "There is no codi brand available",
                "data": null
            }
            ```

### 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회

- uri: `/api/codi/categories/{category}/withMinMaxPrice`
- uri parameters
    - category: top, outerware, pants, sneakers, bag, hat, socks, accessory 중에 하나 선택 가능
- response
    - 성공
        - status code: `200`
        - response body exapmle

            ```json
            {
                "success": true,
                "errorMessage": null,
                "data": {
                    "category": "TOP",
                    "minPrice": [
                        {
                            "brandId": 3,
                            "brandName": "C",
                            "price": 10000
                        }
                    ],
                    "maxPrice": [
                        {
                            "brandId": 9,
                            "brandName": "I",
                            "price": 11400
                        }
                    ]
                }
            }
            ```
    - 잘못된 카테고리
        - status code: `400`
        - response body example

            ```json
            {
                "success": false,
                "errorMessage": "top1 is not proper category.",
                "data": null
            }
            ```

