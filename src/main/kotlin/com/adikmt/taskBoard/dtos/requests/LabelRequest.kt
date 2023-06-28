package com.adikmt.taskBoard.dtos.requests

import jakarta.validation.constraints.NotNull


class LabelRequest(
    @NotNull
    val name: String,
    @NotNull
    val colour: String
)