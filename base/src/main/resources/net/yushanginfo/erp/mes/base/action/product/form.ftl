[#ftl]
[@b.head/]
[@b.toolbar title="修改产品信息"]bar.addBack();[/@]
[@b.tabs]
 [@b.tab label="基本信息"]
  [@b.form action=b.rest.save(product) theme="list"]
    [@b.textfield name="product.code" label="产品编号" value="${product.code!}" required="true" maxlength="30"/]
    [@b.textfield name="product.specification" label="产品图号" value=product.specification!  required="true" maxlength="80"/]
    [@b.textfield name="product.name" label="产品名称" value="${product.name!}" required="true" maxlength="80"/]
    [@b.select name="product.materialType.id" label="品号类别" value=product.materialType! required="true" items=materialTypes empty="..." option=r"${item.code} ${item.name}" /]
    [@b.select name="product.unit.id" label="计量单位" value= product.unit! required="true" items=units empty="..."/]
    [@b.textfield name="product.remark" label="备注" value="${product.remark!}" maxlength="190"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
 [/@]
[#if product.persisted]
 [@b.tab label="材料清单"]
   [@b.div href="product-bom!search?item.product.id=${product.id}"/]
 [/@]
[/#if]
[/@]

[@b.foot/]
