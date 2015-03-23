<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<div id="tablecontent">
	<pg:beaninfo requestKey="table">
		<!-- BEGIN FORM-->

		<form action="#" class="horizontal-form">
			<div class="form-body">
				<h3 class="form-section">基本信息设置</h3>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">First Name</label> <input
								type="text" id="firstName" class="form-control"
								placeholder="Chee Kin"> <span class="help-block">This
								is inline help</span>
						</div>
					</div>
					<!--/span-->
					<div class="col-md-6">
						<div class="form-group has-error">
							<label class="control-label">Last Name</label> <input type="text"
								id="lastName" class="form-control" placeholder="Lim"> <span
								class="help-block">This field has error.</span>
						</div>
					</div>
					<!--/span-->
				</div>
				<!--/row-->
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">Gender</label> <select
								class="form-control">
								<option value="">Male</option>
								<option value="">Female</option>
							</select> <span class="help-block">Select your gender</span>
						</div>
					</div>
					<!--/span-->
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">Date of Birth</label> <input
								type="text" class="form-control" placeholder="dd/mm/yyyy">
						</div>
					</div>
					<!--/span-->
				</div>
				<!--/row-->
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">Category</label> <select
								class="select2_category form-control"
								data-placeholder="Choose a Category" tabindex="1">
								<option value="Category 1">Category 1</option>
								<option value="Category 2">Category 2</option>
								<option value="Category 3">Category 5</option>
								<option value="Category 4">Category 4</option>
							</select>
						</div>
					</div>
					<!--/span-->
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">Membership</label>
							<div class="radio-list">
								<label class="radio-inline"> <input type="radio"
									name="optionsRadios" id="optionsRadios1" value="option1"
									checked> Option 1
								</label> <label class="radio-inline"> <input type="radio"
									name="optionsRadios" id="optionsRadios2" value="option2">
									Option 2
								</label>
							</div>
						</div>
					</div>
					<!--/span-->
				</div>
				<!--/row-->
				<h3 class="form-section">表字段配置</h3>
				<div class="row">
					<div class="col-md-12 ">
						<div class="form-group">
							<div class="note note-success">
								<p>设置表单字段</p>
							</div>
							<table
								class="table table-bordered table-striped table-condensed flip-content">
								<thead class="flip-content">
									<tr>
										<th>字段名称</th>
										<th>中文名称</th>
										<th>字段类型</th>
										<th>java类型</th>
										<th>必填项</th>
										<th>类型校验</th>
										<th>日期格式</th>
										<th>查询条件</th>
										<th>排序字段</th>

									</tr>
								</thead>
								<tbody>

									
										<tr>
											<td>AAC</td>
											<td>AUSTRALIAN AGRICULTURAL COMPANY LIMITED.</td>
											<td class="numeric">$1.38</td>
											<td class="numeric">-0.01</td>
											<td class="numeric">-0.36%</td>
											<td class="numeric">$1.39</td>
											<td class="numeric">$1.39</td>
											<td class="numeric">$1.38</td>
											<td class="numeric">9,395</td>
										</tr>
									
									<tr>
										<td>AAD</td>
										<td>ARDENT LEISURE GROUP</td>
										<td class="numeric">$1.15</td>
										<td class="numeric">+0.02</td>
										<td class="numeric">1.32%</td>
										<td class="numeric">$1.14</td>
										<td class="numeric">$1.15</td>
										<td class="numeric">$1.13</td>
										<td class="numeric">56,431</td>
									</tr>
									
									<tr>
										<td>ACR</td>
										<td>ACRUX LIMITED</td>
										<td class="numeric">$3.71</td>
										<td class="numeric">+0.01</td>
										<td class="numeric">0.14%</td>
										<td class="numeric">$3.70</td>
										<td class="numeric">$3.72</td>
										<td class="numeric">$3.68</td>
										<td class="numeric">191,373</td>
									</tr>

								</tbody>
							</table>
						</div>
					</div>
				</div>

			</div>
			<div class="form-actions right">
				<button type="button" class="btn default">Cancel</button>
				<button type="submit" class="btn blue">
					<i class="fa fa-check"></i> Save
				</button>
			</div>

		</form>


	</pg:beaninfo>
</div>