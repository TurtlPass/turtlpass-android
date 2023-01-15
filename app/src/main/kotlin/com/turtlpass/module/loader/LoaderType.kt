package com.turtlpass.module.loader

import com.airbnb.lottie.compose.LottieConstants

sealed class LoaderType {

    abstract val minFrame: Int

    abstract val maxFrame: Int

    abstract val iterations: Int


    object Loading : LoaderType() {

        override val minFrame: Int
            get() = 0

        override val maxFrame: Int
            get() = 120

        override val iterations: Int
            get() = LottieConstants.IterateForever
    }

    object Success : LoaderType() {

        override val minFrame: Int
            get() = 239

        override val maxFrame: Int
            get() = 400

        override val iterations: Int
            get() = 1
    }

    object Error : LoaderType() {

        override val minFrame: Int
            get() = 658

        override val maxFrame: Int
            get() = 822

        override val iterations: Int
            get() = 1
    }
}
