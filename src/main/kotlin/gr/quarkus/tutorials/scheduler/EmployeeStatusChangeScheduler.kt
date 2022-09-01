package gr.quarkus.tutorials.scheduler

import gr.quarkus.tutorials.entities.Status
import gr.quarkus.tutorials.repositories.EmployeeRepository
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.quarkus.logging.Log
import io.quarkus.scheduler.Scheduled
import io.smallrye.mutiny.Uni
import org.hibernate.reactive.mutiny.Mutiny
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.context.control.ActivateRequestContext


@ApplicationScoped
@ActivateRequestContext
class EmployeeStatusChangeScheduler(
    private val repository: EmployeeRepository ) {

    var currentStatus: Status = Status.WORKING
    var changedStatus: Status = Status.NOT_WORKING

//    @ReactiveTransactional
//    @Scheduled(every = "10s")
//    fun changeStatus(): Uni<Void> {
//        currentStatus = if (currentStatus == Status.WORKING) Status.NOT_WORKING else Status.WORKING //working
//        changedStatus = if (changedStatus == Status.WORKING) Status.NOT_WORKING else Status.WORKING //not-working
//        return repository.update("status=?1 where status=?2", changedStatus, currentStatus)
//            .onItem()
//            .invoke { it ->
//                if (it != 0) Log.info("[***] Status changed to [$changedStatus] for $it Employees")
//                else Log.info("[---] There was no change in Employees for status...Please take a look")
//            }
//            .onItem()
//            .ignore()
//            .andSwitchTo(Uni.createFrom().voidItem())
//    }

}