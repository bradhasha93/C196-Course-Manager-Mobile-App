PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE mentors (mentorId INTEGER PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL, name STRING, phone STRING, email STRING);
CREATE TABLE assessments (assessmentId INTEGER PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL, type STRING, title, dueDate, goalDate, goalDateAlert BOOLEAN DEFAULT false);
CREATE TABLE courses (courseId INTEGER PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL, title STRING, status STRING, startDate STRING, startDateAlert BOOLEAN DEFAULT false, endDate STRING, endDateAlert BOOLEAN DEFAULT false, notes STRING);
CREATE TABLE terms (termId INTEGER PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT, title STRING, startDate STRING, endDate STRING);
CREATE TABLE term_courses (termId INTEGER REFERENCES terms (termId), courseId INTEGER REFERENCES courses (courseId));
CREATE TABLE course_mentors (courseId INTEGER REFERENCES courses (courseId), mentorId INTEGER REFERENCES mentors (mentorId));
CREATE TABLE course_assessments (courseid INTEGER REFERENCES courses (courseId), assessmentId INTEGER REFERENCES assessments (assessmentId));
COMMIT;