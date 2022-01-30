<h1 align="center">
  Heycar listing API
</h1>
<p align="center">With listing-api a dealer can upload new listings (JSON and CSV format) and user can query listing with dealerId or search all listings by make, model, color and year</p>

## ⚡️ Quick start

> First of all, clone project to your local directory.

```bash
git clone git@github.com:layemut/listing-api.git
```

> Then, cd into project directory.

```bash
cd listing-api
```

> In project directory, execute the following command to test and package API into jar

```bash
mvn package
```

> After succesful packaging, there should be a jar in targer directory. Now execute the following command to run the API

```bash
docker-compose up --build -d
```

That's all, you now have listing-api running locally on port 8080! 🎉

## 📖 Documentation

You can access the documentation after running the API on http://localhost:8080/swagger-ui.html

> Example JSON listing request

```json
[
    {
        "code": "1",
        "make": "BMW",
        "model": "M3",
        "year": 2022,
        "color": "Red",
        "price": 10000,
        "kw": 200
    },
    {
        "code": "2",
        "make": "VW",
        "model": "GOLF",
        "year": 2021,
        "color": "Black",
        "price": 9000,
        "kw": 160
    }
]
```

> Example CSV listing request

[valid csv file](https://github.com/layemut/listing-api/blob/869aabe14b9783100f3df7ec82971cf7d9eb7430/src/test/resources/valid_listings.csv)


## 🚏 Roadmap

- [ ] Use redis(or alternative) for distributed cache.
- [ ] Internationalization for error messages maybe.
- [ ] Use pagination for listing and search. (data might get too big)
- [ ] Test coverage to %100 :)
- [ ] Use mapstruct(or alternative) for mapping from/to dto and entity. (for readability)
