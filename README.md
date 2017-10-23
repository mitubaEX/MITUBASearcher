## MITUBASearcher

## How to use

### create Solr server with maven central

```
rake crawler
```

very slow!

### create Solr server with local files

```
mv <your files> ./data
rake
```

## routes

| Method | Route   | Describe                  | Param                        |
|--------|---------|---------------------------|------------------------------|
| POST   | /search | return search result json | {data, birthmark, threshold} |
| GET    | /       | home                      | {}                           |



