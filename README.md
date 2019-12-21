# NHentai-API-Java

NHentai API implementation in Java, a extended project from NHViewer

The project include functions/API I used in NHViewer

## Usage

[App.java](src/main/java/com/github/ttdyce/App.java) should be a great demo of the API

The API use callback methods to operate, here is a example to get index comic list

```java
NHAPI api = new NHAPI();
api.getComicList(new ResponseCallback() {
    @Override
    public void onReponse(String response) {
        JsonArray array = new JsonParser().parse(response).getAsJsonArray();
        Gson gson = new Gson();
        
        for (JsonElement jsonElement : array) {
            Comic c = gson.fromJson(jsonElement, Comic.class);
            list.add(c);
        }

    }
});

for (Comic comic : list) {
    System.out.println(comic.getTitle());
}
```
