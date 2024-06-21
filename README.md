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
<p>Description: Аутентификация пользователя</p>

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
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">POST</span> https://localhost:8443/place/hall
</summary>
<p>Description: Позволяет создать зал площадки организации</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
     "hall_number": Integer,
     "hall_name": String,
     "number_of_seats": Integer,
     "info": String,
     "seat_group_info": [String],
     "place_id": Long
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
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/place/city
</summary>
<p>Description: Вывод всех городов</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{

}
```
ResponseBody
```
{
    "cityResponses": 
    [
    {
    "Id": Long,
    "name_rus": String,
    "name_eng": String
    }, ...
    ]
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
        "signature": String,
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
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/event/{cityName}
</summary>
<p>Description: Вывод 10 мероприятий по городу</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "cityName": String,
    "offset": Integer,
    "date": Date
}
```
ResponseBody
```
{
    "public_events":
    [
    {
        "id": Long,
        "name_rus": String,
        "symbolic_name": String,
        "age_rating": Integer,
        "genres": [String],
        "img": String,
        "type_event": String,
        "favorite": Boolean,
        "description": String
    }, ...
    ]
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/event/{cityName}/announcement
</summary>
<p>Description: Вывод 10 мероприятий по городу и будущим сеансам, которых не было</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "cityName": String,
    "offset": Integer,
    "date": Date
}
```
ResponseBody
```
{
    "public_events":
    [
    {
        "id": Long,
        "name_rus": String,
        "symbolic_name": String,
        "age_rating": Integer,
        "genres": [String],
        "img": String,
        "type_event": String,
        "favorite": Boolean,
        "description": String
    }, ...
    ]
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/event/{cityName}/{eventName}
</summary>
<p>Description: Вывод полной информации о мероприятии по id и символьному названию</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "cityName": String,
    "eventName": String,
    "date": Date
}
```
ResponseBody
```
{
    "public_events":
    {
        "id": Long,
        "name_rus": String,
        "symbolicName": String,
        "description": String,
        "duration": String,
        "rating": Double,
        "age_rating": Integer,
        "genres": [String],
        "pushkin": Boolean,
        "img": String,
        "type_event": String,
        "writer_or_artist": String,
        "actors": [String],
        "tags": [String],
        "favorite": Boolean,
        "sessions_info":
        {
            "place_name": String,
            "address": String,
            "sessions":
            {
            "id": Long,
            "price": Double,
            "start_time": LocalDateTime,
            "message_status": Boolean
            }
        }
    }
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/event/img/{id}>>{symbolicName}
</summary>
<p>Description: Вывод изображения мероприятия</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "id": String,
    "symbolicName": String
}
```
ResponseBody
```
{
    "img": String
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/event/img/{id}>>{symbolicName}
</summary>
<p>Description: Вывод изображения мероприятия</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "id": String,
    "symbolicName": String
}
```
ResponseBody
```
{
    "img": String
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/event/{cityName}/age
</summary>
<p>Description: Вывод мероприятий по указанному возрасту</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "cityName": String,
    "age": int,
    "offset": Integer,
    "date": Date
}
```
ResponseBody
```
{
     "public_events":
    [
    {
        "id": Long,
        "name_rus": String,
        "symbolic_name": String,
        "age_rating": Integer,
        "genres": [String],
        "img": String,
        "type_event": String,
        "favorite": Boolean,
        "description": String
    }, ...
    ]
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/event/{cityName}/type
</summary>
<p>Description: Вывод мероприятий по типу</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "cityName": String,
    "type": String,
    "offset": Integer,
    "date": Date
}
```
ResponseBody
```
{
     "public_events":
    [
    {
        "id": Long,
        "name_rus": String,
        "symbolic_name": String,
        "age_rating": Integer,
        "genres": [String],
        "img": String,
        "type_event": String,
        "favorite": Boolean,
        "description": String
    }, ...
    ]
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/event/{cityName}/genre
</summary>
<p>Description: Вывод мероприятий по указанным жанрам</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "cityName": String,
    "genre": String,
    "offset": Integer,
    "date": Date
}
```
ResponseBody
```
{
     "public_events":
    [
    {
        "id": Long,
        "name_rus": String,
        "symbolic_name": String,
        "age_rating": Integer,
        "genres": [String],
        "img": String,
        "type_event": String,
        "favorite": Boolean,
        "description": String
    }, ...
    ]
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/event/genre
</summary>
<p>Description: Позволяет вывести все возможные жанры</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{

}
```
ResponseBody
```
{
     "genres"
    [
    {
        "id": Long,
        "genre_name": String
    }, ...
    ]
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/event/type
</summary>
<p>Description: Позволяет вывести все возможные типы мероприятий</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{

}
```
ResponseBody
```
{
     "types"
    [
    {
        "id": Long,
        "type_name": String
    }, ...
    ]
}
```
</details>

---
## Administrator

<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">POST</span> https://localhost:8443/dGlja2V0QWRtaW4=
</summary>
<p>Description: Регистрация администратора</p>
<p>Authorization - TRUE</p>

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
    "token": String
}
```
</details>

---
## Client

<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: orange; color: white; padding: 5px 10px; border-radius: 5px">PUT</span> https://localhost:8443/client/favorite
</summary>
<p>Description: Изменение избранного для пользователя</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "id": Long
}
```
ResponseBody
```
{
    "event_id": Long,
    "favorite": Boolean
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/client/favorite
</summary>
<p>Description: Получение массива избранного</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{

}
```
ResponseBody
```
{
     "public_events":
    [
    {
        "id": Long,
        "name_rus": String,
        "symbolic_name": String,
        "age_rating": Integer,
        "genres": [String],
        "img": String,
        "type_event": String,
        "favorite": Boolean,
        "description": String
    }, ...
    ]
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: orange; color: white; padding: 5px 10px; border-radius: 5px">PUT</span> https://localhost:8443/client/rating
</summary>
<p>Description: Позволяет оценить мероприятие пользователю</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "event": String,
    "rating_client": String
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
## Organization

<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/organization
</summary>
<p>Description: Позволяет получить организацию по токену</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{

}
```
ResponseBody
```
{
    "id": Long,
    "user": 
    {
        "id": Long,
        "email": String,
        "password": String,
        "role": String,
        "status": String,
        "active_code": String,
    }
    "inn": String (max size 10),
    "kbk": String (max size 20),
    "kpp": String (max size 9),
    "ogrn": String (max size 13),
    "oktmo": String (max size 11),
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

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/organization/events
</summary>
<p>Description: Позволяет вывести мероприятии у организации и общее их количество</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{

}
```
ResponseBody
```
{
    "total": Integer,
    "events":
    {
        "id": Long,
        "name": String,
        "symbolic_name": String,
        "rating": Double,
        "duration": String,
        "pushkin": Boolean,
        "tags":
        {
            "id": Long,
            "name": String,
        },
        "genres":
        {
            "id": Long,
            "genre_name": String,
        },
        "age_rating": String,
        "total_sessions": Integer
    }
    
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/organization/session_sum
</summary>
<p>Description: Вывод количества сеансов на день по площадкам</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{

}
```
ResponseBody
```
{
    "placesByOrganization": 
    [
    {
        "place_name": String,
        "city_name": String,
        "place_address": String,
        "events":
        [
        {
            "event_name": String,
            "type": String,
            "sessions":
            [
            {
                "sales": Integer,
                "onSales": Integer,
                "startTime": String,
                "finishTime": String,
                "numberOfViews": Integer,
                "price": Double,
                "Boolean": Integer
            }, ...
            ]
        }, ...
        ]
    }, ...
    ]
    
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/organization/places
</summary>
<p>Description: Позволяет вывести площадки у организации и количество залов</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{

}
```
ResponseBody
```
{
    "total": Integer,
    "places": 
    [
    {
        "id": Long,
        "address": String,
        "city": String,
        "place_name": String,
        "total_hall": Integer
    }, ...
    ]
    
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">GET</span> https://localhost:8443/organization/halls
</summary>
<p>Description: Вывод залов по площадкам организации</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{

}
```
ResponseBody
```
{
    "places": 
    [
    {
        "id": Long,
        "place_name": String,
        "address": String,
        "city": String,
        "halls":
        [
        {
            "hall_id": Long,
            "hall_number": Integer,
            "hall_name": String,
            "seats": Integer,
            "info": String,
            "status": boolean
        }, ...
        ]
    }, ...
    ]
}
```
</details>

---
## Session

<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">POST</span> https://localhost:8443/session
</summary>
<p>Description: Позволяет создать сеанс меоприятия</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "start_time": String,
    "price": Double,
    "event_id": Long,
    "hall_id": Long,
    "type_of_movie": String
}
```
ResponseBody
```
{
    "message": String
}
```
</details>