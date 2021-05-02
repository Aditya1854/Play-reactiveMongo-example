//package repository
//import org.scalatestplus.play.PlaySpec
//import org.scalatestplus.play.guice.GuiceOneAppPerTest
//import play.api.test.Helpers._
//import play.api.test.{Injecting, WithApplication}
//import org.mockito.MockitoSugar
//import models._
//import reactivemongo.api.commands.DefaultWriteResult
//import scala.concurrent.Future
//
//class StudentRepositorySpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest{
//
//  implicit val mockedRepo: StudentRepository = mock[StudentRepository]
//  "Student repository" should {
//
//    "get all rows" in new WithStudentRepository() {
//      val student= Student(1,"aditya","aditya@gmail.com",1,"1995-05-06")
//      when(mockedRepo.createStudent(student)) thenReturn Future.successful(DefaultWriteResult(true,1,List(),None,None,None))
//      val result = await(studentRepo.findAll())
//      result.length mustBe 2
//      result.head mustBe Seq(Student(1,"aditya","aditya@gmail.com",1,"1995-05-06"))
//    }
//  }
//}
//
//
//
//trait WithStudentRepository extends WithApplication with Injecting {
//
//  val studentRepo = inject[StudentRepository]
//}
