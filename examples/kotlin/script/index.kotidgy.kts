import com.meiblorn.kotidgy.dsl.*

kotidgy {
    templates {
        t { +"Hello" }
        t { +"Hi" / "Aloha" + ", man !" }
        t { +f { 2..3 } + " apples " + "on the " + "plate" / "table" }
        t { +f { all { 2..3 } } + " apples " + "on the " + "plate" / "table" }
    }
}