package pl.lonski.edunomator.common.physics;

public class BodyUserData {

	private boolean deleteFlag;

	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	public BodyUserData setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
		return this;
	}
}

