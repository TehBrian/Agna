rootProject.name = "agna"

projects(
	"configurate",
	"paper",
)

fun projects(vararg names: String) {
	include(*names)

	names.forEach {
		project(":$it").name = "agna-$it"
	}
}
