package com.jhh.id.generator;

import java.util.concurrent.TimeUnit;

public interface IGenerator {

  Number nextId(long timeout, TimeUnit unit) throws InterruptedException;

}
