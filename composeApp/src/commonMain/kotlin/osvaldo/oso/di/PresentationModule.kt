package osvaldo.oso.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import osvaldo.oso.domain.usecase.DeleteCharacterUseCase
import osvaldo.oso.domain.usecase.GetCharactersUseCase
import osvaldo.oso.domain.usecase.GetCharacterUseCase
import osvaldo.oso.domain.usecase.SaveCharacterUseCase
import osvaldo.oso.presentation.viewmodel.home.HomeViewModel

val presentationModule = module {
    singleOf(::GetCharactersUseCase).bind()
    singleOf(::GetCharacterUseCase).bind()
    singleOf(::SaveCharacterUseCase).bind()
    singleOf(::DeleteCharacterUseCase).bind()
    viewModelOf(::HomeViewModel)
}