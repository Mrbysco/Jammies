package com.mrbysco.jammies.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class DancingData implements IDancingMob {
	public static final MapCodec<DancingData> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
							Codec.BOOL.fieldOf("dancing").forGetter(p_311729_ -> p_311729_.dancing),
							Codec.LONG.fieldOf("accumulatedTime").forGetter(p_311729_ -> p_311729_.accumulatedTime),
							Codec.LONG.fieldOf("lastTime").forGetter(p_311729_ -> p_311729_.lastTime)
					)
					.apply(instance, DancingData::new)
	);

	public long lastTime = Long.MAX_VALUE;
	public long accumulatedTime;
	public boolean dancing;

	public DancingData(boolean dancing, long accumulatedTime, long lastTime) {
		this.dancing = dancing;
		this.accumulatedTime = accumulatedTime;
		this.lastTime = lastTime;
	}

	public DancingData(boolean dancing) {
		this(dancing, 0L, Long.MAX_VALUE);
	}

	public void start(int i) {
		this.lastTime = (long) i * 1000L / 20L;
		this.accumulatedTime = 0L;
	}

	public void startIfStopped(int i) {
		if (!this.isStarted()) {
			this.start(i);
		}
	}

	public void animateWhen(boolean condition, int tickCount) {
		if (condition) {
			this.startIfStopped(tickCount);
		} else {
			this.stop();
		}
	}

	public void stop() {
		this.lastTime = Long.MAX_VALUE;
	}

	public void ifStarted(Consumer<IDancingMob> state) {
		if (this.isStarted()) {
			state.accept(this);
		}
	}

	public void updateTime(float p_216975_, float p_216976_) {
		if (this.isStarted()) {
			long i = Mth.lfloor(p_216975_ * 1000.0F / 20.0F);
			this.accumulatedTime += (long) ((float) (i - this.lastTime) * p_216976_);
			this.lastTime = i;
		}
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public void setAccumulatedTime(long accumulatedTime) {
		this.accumulatedTime = accumulatedTime;
	}

	public long getAccumulatedTime() {
		return this.accumulatedTime;
	}

	public boolean isStarted() {
		return this.lastTime != Long.MAX_VALUE;
	}

	@Override
	public boolean isDancing() {
		return this.dancing;
	}

	@Override
	public void setDancing(boolean dancing) {
		this.dancing = dancing;
	}
}
