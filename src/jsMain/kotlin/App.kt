import auth.authProvider
import component.group.CCurrentStudent
import component.lesson.CLessonAdd
import component.lesson.CLessonEditContainer
import component.lesson.CLessonInList
import component.role.CRoleAdd
import component.role.CRoleEdit
import component.role.CRoleInList
import component.student.CStudentAdd
import component.student.CStudentEdit
import component.student.CStudentInList
import component.template.RestContainerChildProps
import component.template.restContainer
import component.template.restList
import react.FC
import react.Props
import react.create
import react.createContext
import react.dom.client.createRoot
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter
import react.router.dom.Link
import ru.altmanea.webapp.access.Role
import ru.altmanea.webapp.access.Token
import ru.altmanea.webapp.access.User
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Lesson
import ru.altmanea.webapp.data.Student
import tanstack.query.core.QueryClient
import tanstack.query.core.QueryKey
import tanstack.react.query.QueryClientProvider
import tanstack.react.query.devtools.ReactQueryDevtools
import web.dom.document

typealias  UserInfo = Pair<User, Token>?

val invalidateRepoKey = createContext<QueryKey>()
val userInfoContext = createContext<UserInfo>(null)

fun main() {
    val container = document.getElementById("root")!!
    createRoot(container).render(app.create())
}

val app = FC<Props>("App") {
    HashRouter {
        authProvider {
            QueryClientProvider {
                client = QueryClient()
                ul {
                    listOf("Students", "Lessons", "Roles").map { tag ->
                        li {
                            Link {
                                +tag
                                to = tag.lowercase()
                            }
                        }
                    }
                }

                Routes {
                    Route {
                        path = "lessons"
                        val list: FC<RestContainerChildProps<Lesson>> =
                            restList(
                                CLessonInList,
                                CLessonAdd,
                                CLessonEditContainer
                            )
                        element = restContainer(
                            Config.lessonsPath,
                            list,
                            "lessons"
                        ).create()
                    }
                    Route {
                        path = "students"
                        val list: FC<RestContainerChildProps<Student>> =
                            restList(
                                CStudentInList,
                                CStudentAdd,
                                CStudentEdit
                            )
                        element = restContainer(
                            Config.studentsPath,
                            list,
                            "students"
                        ).create()
                    }
                    Route {
                        path = "roles"
                        val list: FC<RestContainerChildProps<Role>> =
                            restList(
                                CRoleInList,
                                CRoleAdd,
                                CRoleEdit
                            )
                        element = restContainer(
                            Config.rolesPath,
                            list,
                            "roles"
                        ).create()
                    }
                    Route {
                        path = "students/" + ":name"
                        element = CCurrentStudent.create()
                    }
                }
                ReactQueryDevtools { }
            }
        }
    }
}