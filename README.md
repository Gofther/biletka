# Project Methods

(~) - Необязательный пункт

## Security

<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">POST</span> https://localhost:8443/security
</summary>
<p>Description: Регистрация обычного пользователя</p>

---
RequestBody
```
{
    "email": String,
    "password": String,
    "full_name": String,
    "phone_number": String,
    "birthday": Date
}
```
ResponseBody
```
{
    "message": String
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">POST</span> https://localhost:8443/security/auth
</summary>
<p>Description: Регистрация пользователя</p>

---
RequestBody
```
{
    "email": String,
    "password": String,
    "role": String
}
```
ResponseBody
```
{
    "message": String
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">POST</span> https://localhost:8443/security/organization
</summary>
<p>Description: Регистрация организации</p>

---
RequestBody
```
{
    "email": String,
    "password": String,
    "contact_phone": String,
    "full_name_organization": String,
    "full_name_signatory": String,
    "inn": String (max size 10),
    "kbk": String (max size 20),
    "kpp": String (max size 9),
    "ogrn": String (max size 13),
    "oktmo": String (max size 11),
    "legal_address": String,
    "name_payer": String,
    "position_signatory": String,
    "postal_address": String (max size 6)
}
```
ResponseBody
```
{
    "message": String
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: orange; color: white; padding: 5px 10px; border-radius: 5px">PUT</span> https://localhost:8443/security/active
</summary>
<p>Description: Активация пользователя</p>

---
RequestBody
```
{
    "email": String,
    "code": String
}
```
ResponseBody
```
{
    "message": String
}
```
</details>

---
## Place

<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">POST</span> https://localhost:8443/place
</summary>
<p>Description: Создания площадки и добавления её к организации</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "address": String,
    "place_name": String,
    "city": String
}
```
ResponseBody
```
{
    "message": String
}
```
</details>

---
## Event

<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">POST</span> https://localhost:8443/event
</summary>
<p>Description: Создание мероприятия и добавления его к организации</p>
<p>Authorization - TRUE</p>

---
RequestPart - File

---
RequestPart - String
```
{
    "event_basic": {
        "name": String,
        "name_rus": String,
        "organizaer": String,
        "age_rating": String,
        "type_event": String,
        "pushkin": Boolean,
        "eventIdCulture": Long, (~)
        "show_in_poster": Boolean,
        "genres": [String]
    },
    "event_additional": {
        "author": String,
        "director": String,
        "writer_or_artist": String,
        "actors": [String],
        "tags": [String]
    },
    "event_wev_widget": {
        "description": String,
        "link": "https://afisha.yandex.ru/" + String,
        "signature": String
    },
    "duration": String
}
```
ResponseBody
```
{
    "message": String
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/event
</summary>
<p>Description: Вывод мероприятия по id</p>
<p>Authorization - TRUE</p>


```
{
    "id": Long
}
```
ResponseBody
```
{
    "Id": Long,
    "symbolicName": String,
    "name_rus": String,
    "rating": Double,
    "description": String,
    "type_event": String,
    "ageRating": int,
    "genres": String[],
    "author": String,
    "writerOrArtist": String,
    "duration": String,
    "tags": String[],
    "pushkin": Boolean
}
```
</details>

---
## Organization
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/organization
</summary>
<p>Description: Вывод организации</p>
<p>Authorization - TRUE</p>


```
{
}
```
ResponseBody
```
{
    "id": Long,
    "user": {
        "id": Long,
        "email": String,
        "password": String,
        "role": String,
        "status": String,
        "activeCode": String
    },
    "inn": String,
    "kbk": String,
    "kpp": String,
    "ogrn": String,
    "oktmo": String,
    "contactPhone": String,
    "email": String,
    "fullNameOrganization": String,
    "fullNameSignatory": String,
    "legalAddress": String,
    "namePayer": String,
    "positionSignatory": String,
    "postalAddress": Integer
}
```
</details>