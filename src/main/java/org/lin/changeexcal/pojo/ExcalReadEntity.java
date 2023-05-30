package org.lin.changeexcal.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.lin.changeexcal.annotation.ExcelField;
import org.lin.changeexcal.annotation.ExcelTable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ExcelTable(name = {"通过名单", "未通过名单"}, title = {'A', 'N'})
public class ExcalReadEntity {

    @ExcelField(value = "A", name = "序号")
    private Cell serialNumber;
    @ExcelField(value = "B", name = "市")
    private Cell city;
    @ExcelField(value = "C", name = "乡镇")
    private Cell township;
    @ExcelField(value = "D", name = "行政村")
    private Cell administrativeVillage;
    @ExcelField(value = "E", name = "父/母姓名")
    private Cell parentName;
    @ExcelField(value = "F", name = "联系电话")
    private Cell phone;
    @ExcelField(value = "G", name = "婴幼儿姓名")
    private Cell sonName;
    @ExcelField(value = "H", name = "出生日期")
    private Cell birthday;
    @ExcelField(value = "I", name = "贫困属性")
    private Cell poorState;
    @ExcelField(value = "J", name = "福利主任姓名")
    private Cell welfareName;
    @ExcelField(value = "K", name = "是否参与摸底调研")
    private Cell participateBottom;
    @ExcelField(value = "L", name = "审批情况")
    private Cell approvalSituation;
    @ExcelField(value = "M", name = "审批结果")
    private Cell approvalResult;
    @ExcelField(value = "N", name = "备注")
    private Cell remarks;
}
