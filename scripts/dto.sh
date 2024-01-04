#!/usr/bin/env bash
java -jar openapi-generator-cli-7.0.0.jar generate \
  -i https://docs.culture.ru/tickets/#/%D0%9A%D0%BE%D0%BD%D1%82%D1%80%D0%BE%D0%BB%D1%91%D1%80%D1%8B%20%D0%B1%D0%B8%D0%BB%D0%B5%D1%82%D0%BE%D0%B2/put_events__event_id__tickets__barcode__visit \
  -g java \
  -o spring-openapi-generator-api-client