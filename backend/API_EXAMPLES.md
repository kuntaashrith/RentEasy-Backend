## Auth

### Register

`POST /api/auth/register`

Request:

```json
{
  "name": "Asha Owner",
  "email": "owner@renteasy.com",
  "password": "Password@123",
  "phone": "+91-9000000000",
  "role": "OWNER"
}
```

Response (201):

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9....",
  "tokenType": "Bearer",
  "expiresInSeconds": 3600,
  "user": {
    "id": 1,
    "name": "Asha Owner",
    "email": "owner@renteasy.com",
    "phone": "+91-9000000000",
    "role": "OWNER",
    "createdAt": "2026-03-12T08:10:11.121Z"
  }
}
```

### Login

`POST /api/auth/login`

Request:

```json
{
  "email": "owner@renteasy.com",
  "password": "Password@123"
}
```

Response (200): same shape as register.

## Properties

### Create property (OWNER/ADMIN)

`POST /api/properties`

Headers:
- `Authorization: Bearer <token>`

Request:

```json
{
  "title": "2BHK near Metro",
  "description": "Well ventilated, family preferred",
  "city": "Bengaluru",
  "address": "Indiranagar, Bengaluru",
  "rent": 32000.00,
  "bhk": 2,
  "propertyType": "APARTMENT",
  "available": true,
  "imageUrls": [
    "https://cdn.example.com/p/1/1.jpg",
    "https://cdn.example.com/p/1/2.jpg"
  ]
}
```

Response (201):

```json
{
  "id": 10,
  "title": "2BHK near Metro",
  "description": "Well ventilated, family preferred",
  "city": "Bengaluru",
  "address": "Indiranagar, Bengaluru",
  "rent": 32000.00,
  "bhk": 2,
  "propertyType": "APARTMENT",
  "available": true,
  "owner": {
    "id": 1,
    "name": "Asha Owner",
    "phone": "+91-9000000000"
  },
  "imageUrls": [
    "https://cdn.example.com/p/1/1.jpg",
    "https://cdn.example.com/p/1/2.jpg"
  ],
  "createdAt": "2026-03-12T08:20:44.000Z"
}
```

### List properties (paginated)

`GET /api/properties?page=0&size=10`

Response (200):

```json
{
  "content": [
    {
      "id": 10,
      "title": "2BHK near Metro",
      "city": "Bengaluru",
      "rent": 32000.00,
      "bhk": 2,
      "propertyType": "APARTMENT",
      "available": true,
      "thumbnailUrl": "https://cdn.example.com/p/1/1.jpg"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

### Search

`GET /api/properties/search?city=Bengaluru&minRent=20000&maxRent=40000&bhk=2&page=0&size=10`

## Favorites (TENANT)

### Add favorite

`POST /api/favorites/{propertyId}`

### List favorites

`GET /api/favorites`

Response (200):

```json
[
  {
    "id": 5,
    "property": {
      "id": 10,
      "title": "2BHK near Metro",
      "city": "Bengaluru",
      "rent": 32000.00,
      "bhk": 2,
      "propertyType": "APARTMENT",
      "available": true,
      "thumbnailUrl": "https://cdn.example.com/p/1/1.jpg"
    }
  }
]
```

## Inquiries (TENANT creates, OWNER/ADMIN views)

### Send inquiry

`POST /api/inquiries`

Request:

```json
{
  "propertyId": 10,
  "message": "Is this available for April? Can I schedule a visit this weekend?"
}
```

Response (201):

```json
{
  "id": 99,
  "propertyId": 10,
  "tenant": {
    "id": 2,
    "name": "Tina Tenant",
    "phone": "+91-9111111111"
  },
  "message": "Is this available for April? Can I schedule a visit this weekend?",
  "createdAt": "2026-03-12T08:30:00.000Z"
}
```

### List inquiries for a property (OWNER owns that property / ADMIN)

`GET /api/inquiries/property/{propertyId}`

