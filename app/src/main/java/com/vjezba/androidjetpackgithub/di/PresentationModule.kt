/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.vjezba.androidjetpackgithub.di

import androidx.lifecycle.SavedStateHandle
import com.vjezba.androidjetpackgithub.ui.fragments.FlowMultipleExampleFragment
import com.vjezba.androidjetpackgithub.ui.mapper.WeatherViewStateMapper
import com.vjezba.androidjetpackgithub.ui.mapper.WeatherViewStateMapperImpl
import com.vjezba.androidjetpackgithub.ui.utilities.imageLoader.ImageLoader
import com.vjezba.androidjetpackgithub.ui.utilities.imageLoader.ImageLoaderImpl
import com.vjezba.androidjetpackgithub.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
  viewModel { GalleryViewModel(get()) }
  viewModel { (handle: SavedStateHandle) -> LanguagesListViewModel(handle, get()) }
  viewModel { SavedLanguagesListViewModel(get()) }
  viewModel { (languagedId : Int) -> LanguageDetailsViewModel(get(), get(), languagedId) }
  viewModel { LoginViewModel(get()) }
  factory { RegistrationViewModel(get()) }
  viewModel { EnterDetailsViewModel() }
  viewModel { LanguagesActivityViewModel(get()) }
  viewModel { PaggingWithNetworkAndDbViewModel( ) }
  viewModel { PaggingWithNetworkAndDbDataViewModel(get() ) }

  viewModel { FlowWeatherViewModel(get(), get() ) }
  viewModel { FlowMultipleExamplesViewModel() }


  single<ImageLoader> { ImageLoaderImpl() }

  //single { Picasso.get() }

  single<WeatherViewStateMapper> { WeatherViewStateMapperImpl() }

}
