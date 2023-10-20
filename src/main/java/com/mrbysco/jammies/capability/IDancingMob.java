package com.mrbysco.jammies.capability;

import java.util.function.Consumer;

public interface IDancingMob {
	boolean isDancing();

	void setDancing(boolean dancing);

	void setAccumulatedTime(long accumulatedTime);

	void setLastTime(long lastTime);

	void start(int i);

	void startIfStopped(int i);

	void animateWhen(boolean p_252220_, int p_249486_);

	void stop();

	void ifStarted(Consumer<IDancingMob> state);

	void updateTime(float p_216975_, float p_216976_);

	long getAccumulatedTime();

	long getLastTime();

	boolean isStarted();
}
