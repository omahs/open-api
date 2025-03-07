package io.openfuture.api.service

import io.openfuture.api.domain.application.ApplicationAccessKey
import io.openfuture.api.domain.application.ApplicationRequest
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.auth.User
import io.openfuture.api.exception.NotFoundException
import io.openfuture.api.repository.ApplicationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class DefaultApplicationService(
    private val applicationRepository: ApplicationRepository
): ApplicationService {

    override fun getAll(user: User, pageRequest: Pageable): Page<Application> =
        applicationRepository.findAllByUser(user, pageRequest)

    override fun getById(id: Long): Application {
        return applicationRepository.findById(id).orElseThrow {  throw NotFoundException("Not found application with id : $id")}
    }

    override fun getByAccessKey(accessKey: String): Application {
        return applicationRepository.findFirstByApiAccessKey(accessKey).orElseThrow {  throw NotFoundException("Not found application with key : $accessKey")}
    }

    override fun save(request: ApplicationRequest, user: User, applicationAccessKey: ApplicationAccessKey): Application {
        return applicationRepository.save(Application.of(request, user, applicationAccessKey))
    }

    override fun update(id: Long, applicationAccessKey: ApplicationAccessKey): Application {
        val application = applicationRepository.findById(id).orElseThrow()
        application.apiAccessKey =  applicationAccessKey.accessKey
        application.apiSecretKey = applicationAccessKey.secretKey
        return applicationRepository.save(application)
    }

    override fun delete(id: Long) {
        applicationRepository.deleteById(id)
    }
}