package com.epam.songservice.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({First.class, Second.class})
public interface ValidationSequence {}
