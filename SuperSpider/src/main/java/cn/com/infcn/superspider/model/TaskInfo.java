/**
 * 
 */
package cn.com.infcn.superspider.model;

/**
 * @author zyz
 *
 */
// @Entity
// @Table(name = "taskinfo")
// @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
// @DynamicUpdate @DynamicInsert
public class TaskInfo implements java.io.Serializable {
	// Fields
	private static final long serialVersionUID = 1L;
	private String taskName;
	private String taskType;
	private String taskMemo;
	private String creator;
	private String createTime;
	private String planName;
	private String planDescription;
	private String taskState;

	/** default constructor */
	public TaskInfo() {
	}

	/** full constructor */
	public TaskInfo(String taskName, String taskType, String taskMemo,
			String creator, String createTime, String planName,
			String planDescription, String taskState) {
		this.taskName = taskName;
		this.taskType = taskType;
		this.taskMemo = taskMemo;
		this.creator = creator;
		this.createTime = createTime;
		this.planName = planName;
		this.planDescription = planDescription;
		this.taskState = taskState;
	}

	// @Column(name = "taskName")
	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	// @Column(name = "taskType")
	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	// @Column(name = "taskMemo")
	public String getTaskMemo() {
		return this.taskMemo;
	}

	public void setTaskMemo(String taskMemo) {
		this.taskMemo = taskMemo;
	}

	// @Column(name = "creator")
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	// @Column(name = "createTime")
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	// @Column(name = "planName")
	public String getPlanName() {
		return this.planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	// @Column(name = "planDescription")
	public String getPlanDescription() {
		return this.planDescription;
	}

	public void setPlanDescription(String planDescription) {
		this.planDescription = planDescription;
	}

	// @Column(name = "taskState")
	public String getTaskState() {
		return this.taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}
}
