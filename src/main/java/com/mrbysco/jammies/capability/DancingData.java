package com.mrbysco.jammies.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.function.Consumer;

public class DancingData implements IDancingMob, INBTSerializable<CompoundTag> {
	public long lastTime = Long.MAX_VALUE;
	public long accumulatedTime;
	public boolean dancing;

	public DancingData(boolean dancing) {
		this.dancing = dancing;
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

	public void animateWhen(boolean p_252220_, int p_249486_) {
		if (p_252220_) {
			this.startIfStopped(p_249486_);
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

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("dancing", this.isDancing());
		tag.putLong("accumulatedTime", this.getAccumulatedTime());
		tag.putLong("lastTime", this.getLastTime());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.setDancing(nbt.getBoolean("dancing"));
		this.setAccumulatedTime(nbt.getLong("accumulatedTime"));
		this.setLastTime(nbt.getLong("lastTime"));
	}
}
