package it.polimi.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import it.polimi.db.business.ExamRecordBean;

public class ExamRecordDAO {
	private final DataSource dataSrc;
	private static final String DSRC_ERROR = "DataSource not present";
	private static final Logger logger = Logger.getLogger(ExamRecordDAO.class.getName());
	
	public ExamRecordDAO(DataSource userDataSource) {
		dataSrc = userDataSource;
		if(dataSrc == null)
			logger.log(Level.SEVERE, DSRC_ERROR);
	}
	
	public List<ExamRecordBean> getExamRecords(int examId) {
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return Collections.emptyList();
		}
		
		String query = "SELECT * "
		             + "FROM exam_record as record "
		             + "WHERE record.exam_id = ? "
		             + "ORDER BY record.time DESC";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, examId);
			return getExamRecords(statement);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	public static ExamRecordBean createExamRecordBean(ResultSet rs) throws SQLException {
		ExamRecordBean examRecord = new ExamRecordBean();
		examRecord.setId(rs.getInt("record.id"));
		examRecord.setExamId(rs.getInt("record.exam_id"));
		examRecord.setTime(rs.getTimestamp("record.time"));
		return examRecord;
	}

	private List<ExamRecordBean> getExamRecords(PreparedStatement ps) throws SQLException {
		try (ResultSet result = ps.executeQuery()) {
			List<ExamRecordBean> records = new ArrayList<>();
			while(result.next())
				records.add(createExamRecordBean(result));
			return records;
		}
	}
}
