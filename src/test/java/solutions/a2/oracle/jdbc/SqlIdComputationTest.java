/**
 * Copyright (c) 2018-present, A2 Rešitve d.o.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package solutions.a2.oracle.jdbc;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Test;

/**
 *  
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class SqlIdComputationTest {

	@Test
	public void test() {
		final String s_3qkk2t5ybrd94 = "select LANGUAGE_CODE, DESCRIPTION, INSTALLED_FLAG from   FND_LANGUAGES_VL where  INSTALLED_FLAG in ('I', 'B') order by LANGUAGE_CODE";
		final String s_fqrh9j6rrwq2q = "SELECT sequence, compiled_definition FROM   fnd_compiled_id_flexs WHERE  application_id = :fcf_appl AND    id_flex_code = :codename AND    compiler_version_num = :fcf_version ORDER BY sequence ";
		final String s_9s4jf35qdc7kd = "select acc.chart_of_accounts_id ,acc.period_set_name ,acc.accounted_period_type ,acc.security_segment_code into :b0,:b1,:b2,:b3  from gl_access_sets acc where acc.access_set_id=:b4";
		final String s_djj5txv2dzwb6 = "insert into logon (logon_id, customer_id, logon_date) values(logon_seq.nextval,:1 ,:2 )";
		final String s_d6zx7qwmjf7cr = "INSERT INTO ADDRESSES\n"
				+ "        ( address_id,\n"
				+ "          customer_id,\n"
				+ "          date_created,\n"
				+ "          house_no_or_name,\n"
				+ "          street_name,\n"
				+ "          town,\n"
				+ "          county,\n"
				+ "          country,\n"
				+ "          post_code,\n"
				+ "          zip_code\n"
				+ "        )\n"
				+ "        VALUES\n"
				+ "        ( :1 , :2 , TRUNC(SYSDATE,'MI'), :3 , 'Street Name', :4 , :5 , :6 , 'Postcode', NULL)";
		final String s_0qdkf6ckmgm11 = "update GL_INTERFACE \n"
				+ "set status = :status \n"
				+ ", status_description = :description \n"
				+ ", je_batch_id = :batch_id \n"
				+ ", je_header_id = :header_id \n"
				+ ", je_line_num = :line_num \n"
				+ ", code_combination_id_interim = :ccid \n"
				+ ", accounted_dr = :acc_dr \n"
				+ ", accounted_cr = :acc_cr \n"
				+ ", descr_flex_error_message = :descr_description \n"
				+ ", request_id = to_number(:req_id) \n"
				+ "where rowid = :row_id ";
		final String s_88maya168wkas = "update fnd_concurrent_requests  set ofile_size=decode(:b0,'-1',null ,:b0) where request_id=:b2";



		try {
			assertEquals("Must be same!", OraProxyUtils.sql_id(s_3qkk2t5ybrd94), "3qkk2t5ybrd94");
			assertEquals("Must be same!", OraProxyUtils.sql_id(s_fqrh9j6rrwq2q), "fqrh9j6rrwq2q");
			assertEquals("Must be same!", OraProxyUtils.sql_id(s_9s4jf35qdc7kd), "9s4jf35qdc7kd");
			assertEquals("Must be same!", OraProxyUtils.sql_id(s_djj5txv2dzwb6), "djj5txv2dzwb6");
			assertEquals("Must be same!", OraProxyUtils.sql_id(s_d6zx7qwmjf7cr), "d6zx7qwmjf7cr");
			assertEquals("Must be same!", OraProxyUtils.sql_id(s_0qdkf6ckmgm11), "0qdkf6ckmgm11");
			assertEquals("Must be same!", OraProxyUtils.sql_id(s_88maya168wkas), "88maya168wkas");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
