package com.mrbysco.jammies.capability;

import com.mrbysco.jammies.CapabilityHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class DancingCapability implements IDancingMob, ICapabilitySerializable<CompoundTag> {
	public long lastTime = Long.MAX_VALUE;
	public long accumulatedTime;
	public boolean dancing;

	public DancingCapability(boolean dancing) {
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
		return writeNBT(this);
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		readNBT(this, nbt);
	}

	public static CompoundTag writeNBT(IDancingMob instance) {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("dancing", instance.isDancing());
		tag.putLong("accumulatedTime", instance.getAccumulatedTime());
		tag.putLong("lastTime", instance.getLastTime());
		return tag;
	}

	public static void readNBT(IDancingMob instance, CompoundTag tag) {
		instance.setDancing(tag.getBoolean("dancing"));
		instance.setAccumulatedTime(tag.getLong("accumulatedTime"));
		instance.setLastTime(tag.getLong("lastTime"));
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return CapabilityHandler.DANCING_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
	}
}
