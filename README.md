# khtmling-lib
kotlin implementation of the best lib in this world

this lib is not fully java compatible, we have exception with private fields in class, i will fix that in future maybe :D

kotlin example:

```kotlin
@HtmlProperty(
    name = "peoples",
    tag = HtmlTag.DIV,
    styles = [HtmlStyle(CssProperty.BACKGROUND_COLOR, value = "red")]
)
data class People(
    val name: String,
    val age: Int,
    val house: House,
    val jobs: List<Job>
)

data class House(
    val address: String,
    @HtmlIgnore val peopleInHouse: Int = 99
)

data class Job(
    val title: String,
    val salary: Double
)

fun main() {
    val people = People(
        "John",
        24,
        House("via Pushkin, dom Kolotushkina"),
        listOf(
            Job("Developer", 228.322),
            Job("CEO", 99999.999)
        )
    )
    println(people.htmling())
}
```

```html
<div title="peoples" style="background-color: red;">
    <div title="age" >24</div>
    <div title="house" >
        <div title="house" >
            <div title="address" >via Pushkin, dom Kolotushkina</div>
        </div>
    </div>
    <div title="jobs" >
        <div title="job" >
            <div title="salary" >228.322</div>
            <div title="title" >Developer</div>
        </div>
        <div title="job" >
            <div title="salary" >99999.999</div>
            <div title="title" >CEO</div>
        </div>
    </div>    
    <div title="name" >John</div>
</div>

```
