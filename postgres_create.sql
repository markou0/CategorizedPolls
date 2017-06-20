
CREATE TABLE "user" (
	"id" serial NOT NULL,
	"email" varchar NOT NULL UNIQUE,
	"name" varchar NOT NULL UNIQUE,
	"role" varchar NOT NULL DEFAULT 'USER',
	"enabled" integer NOT NULL DEFAULT 1,
	"image" varchar,
	"password" varchar NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);


CREATE TABLE "poll" (
	"id" serial NOT NULL,
	"name" varchar,
	"user_id" integer NOT NULL,
	"poll_theme_id" integer,
	"private" BOOLEAN NOT NULL DEFAULT true,
	"anonymous" BOOLEAN NOT NULL DEFAULT true,
	"show_author" BOOLEAN NOT NULL DEFAULT false,
	"date" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"from_timestamp" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"to_timestamp" TIMESTAMP DEFAULT 'infinity'::TIMESTAMP,
	"hash_tags" TEXT,
	CONSTRAINT poll_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "test_question" (
	"id" serial NOT NULL,
	"text" TEXT NOT NULL,
	"test_id" integer,
	"image" varchar,
	CONSTRAINT test_question_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "community" (
	"id" serial NOT NULL,
	"name" varchar,
	"user_id" integer NOT NULL,
	"created" timestamp DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT community_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "membership" (
	"id" serial NOT NULL,
	"user_id" integer NOT NULL,
	"community_id" integer NOT NULL,
	"accepted" boolean DEFAULT false,
	CONSTRAINT membership_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "test" (
	"id" serial NOT NULL,
	"poll_id" integer NOT NULL,
	CONSTRAINT test_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "category" (
	"id" serial NOT NULL,
	"name" varchar NOT NULL,
	"test_id" integer NOT NULL,
	CONSTRAINT category_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "test_answer" (
	"id" serial NOT NULL,
	"test_question_id" integer NOT NULL,
	"text" TEXT NOT NULL,
	"image" varchar,
	CONSTRAINT test_answer_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "poll_answer" (
	"id" serial NOT NULL,
	"poll_question_id" integer NOT NULL,
	"text" TEXT NOT NULL,
	"other" BOOLEAN NOT NULL DEFAULT false,
	"image" varchar,
	CONSTRAINT poll_answer_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "test_answer_score" (
	"id" serial NOT NULL,
	"test_answer_id" integer NOT NULL,
	"category_id" integer NOT NULL,
	"score" integer NOT NULL DEFAULT 1,
	CONSTRAINT test_answer_score_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "poll_question" (
	"id" serial NOT NULL,
	"text" varchar NOT NULL,
	"poll_id" integer NOT NULL,
	"allow_other_answer" BOOLEAN NOT NULL DEFAULT false,
	"image" varchar,
	CONSTRAINT poll_question_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "poll_theme" (
	"id" serial NOT NULL,
	"name" varchar NOT NULL UNIQUE,
	"description" TEXT,
	"user_id" integer, 
	CONSTRAINT poll_theme_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "test_result" (
	"id" serial NOT NULL,
	"user_id" integer,
	"catagory_id" integer NOT NULL,
	"date" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT test_result_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "poll_result" (
	"id" serial NOT NULL,
	"poll_answer_id" integer NOT NULL,
	"user_id" integer,
	"date" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT poll_result_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);

CREATE TABLE "poll_community" (
	"id" serial NOT NULL,
	"community_id" integer NOT NULL,
	"poll_id" integer NOT NULL,
	CONSTRAINT poll_community_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);

ALTER TABLE "poll_community" ADD CONSTRAINT "poll_community_fk001" FOREIGN KEY ("poll_id") REFERENCES "poll"("id") ON DELETE CASCADE;
ALTER TABLE "poll_community" ADD CONSTRAINT "poll_community_fk002" FOREIGN KEY ("community_id") REFERENCES "community"("id") ON DELETE CASCADE;


ALTER TABLE "poll" ADD CONSTRAINT "poll_fk0" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE CASCADE;
ALTER TABLE "poll" ADD CONSTRAINT "poll_fk1" FOREIGN KEY ("poll_theme_id") REFERENCES "poll_theme"("id") ON DELETE SET NULL;

ALTER TABLE "test" ADD CONSTRAINT "test_fk0" FOREIGN KEY ("poll_id") REFERENCES "poll"("id") ON DELETE CASCADE;

ALTER TABLE "test_result" ADD CONSTRAINT "test_result_fk0" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE SET NULL;
ALTER TABLE "test_result" ADD CONSTRAINT "test_result_fk1" FOREIGN KEY ("catagory_id") REFERENCES "category"("id") ON DELETE CASCADE;

ALTER TABLE "poll_result" ADD CONSTRAINT "poll_result_fk0" FOREIGN KEY ("poll_answer_id") REFERENCES "poll_answer"("id") ON DELETE CASCADE;
ALTER TABLE "poll_result" ADD CONSTRAINT "poll_result_fk1" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE SET NULL;

ALTER TABLE "poll_theme" ADD CONSTRAINT "poll_theme_fk0" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE SET NULL;

ALTER TABLE "test_question" ADD CONSTRAINT "test_question_fk001" FOREIGN KEY ("test_id") REFERENCES "test"("id") ON DELETE CASCADE;

ALTER TABLE "community" ADD CONSTRAINT "community_fk001" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE CASCADE;

ALTER TABLE "membership" ADD CONSTRAINT "membership_fk001" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE CASCADE;
ALTER TABLE "membership" ADD CONSTRAINT "membership_fk002" FOREIGN KEY ("community_id") REFERENCES "community"("id") ON DELETE CASCADE;

ALTER TABLE "category" ADD CONSTRAINT "category_fk001" FOREIGN KEY ("test_id") REFERENCES "test"("id") ON DELETE CASCADE;

ALTER TABLE "test_answer" ADD CONSTRAINT "test_answer_fk001" FOREIGN KEY ("test_question_id") REFERENCES "test_question"("id") ON DELETE CASCADE;

ALTER TABLE "poll_answer" ADD CONSTRAINT "poll_answer_fk001" FOREIGN KEY ("poll_question_id") REFERENCES "poll_question"("id") ON DELETE CASCADE;

ALTER TABLE "test_answer_score" ADD CONSTRAINT "test_answer_score_fk001" FOREIGN KEY ("test_answer_id") REFERENCES "test_answer"("id") ON DELETE CASCADE;
ALTER TABLE "test_answer_score" ADD CONSTRAINT "test_answer_score_fk002" FOREIGN KEY ("category_id") REFERENCES "category"("id") ON DELETE CASCADE;

ALTER TABLE "poll_question" ADD CONSTRAINT "poll_question_fk001" FOREIGN KEY ("poll_id") REFERENCES "poll"("id") ON DELETE CASCADE;

ALTER TABLE "poll_result" ADD CONSTRAINT "poll_result_fk001" FOREIGN KEY ("poll_answer_id") REFERENCES "poll_answer"("id") ON DELETE CASCADE;

INSERT INTO "user" VALUES (0, 'admin', 'admin', 'ADMIN',1,null,'$2a$06$QUb68ga7JjjBGuP.4PMXMO41cHcemjQcdbiin//h329zU3dymXApa');
INSERT INTO "poll_theme" VALUES(0, 'Other', 0);