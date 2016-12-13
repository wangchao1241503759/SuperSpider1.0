	var fieldMappingJsonList = [];

	//定义字段映射对象
	function fieldMappingObject(fieldID,fieldName,fieldExp,filedSource,targetFieldName,targetFieldType)
	{
		this.fieldID = fieldID;
		this.fieldName = fieldName;
		this.fieldExp =  fieldExp;
		this.filedSource = filedSource;
		this.targetFieldName = targetFieldName;
		this.targetFieldType = targetFieldType;
	}
	
	//获取映射字段的所有数据
	function getFieldMappingList()
	{
		
		fieldMappingJsonList = [];
		for (var i = 0; i < paramObjectList.length; i++) {
			var fieldExtractRows = paramObjectList[i].fieldExtractList;
			if (fieldExtractRows.length > 0) {
				for (var j = 0; j < fieldExtractRows.length; j++) {
					var fieldMapping = new fieldMappingObject('',fieldExtractRows[j].name,fieldExtractRows[j].fieldExp,'源字段',fieldExtractRows[j].targetFieldName,fieldExtractRows[j].targetFieldType);
					fieldMappingJsonList.push(fieldMapping);
				}
			}
		}
		
		for(var i = 0 ;i<zhuanRows.length;i++){
			fieldMappingJsonList.push(zhuanRows[i]);
		}
	}