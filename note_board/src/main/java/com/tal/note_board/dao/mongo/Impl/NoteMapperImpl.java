package com.tal.note_board.dao.mongo.Impl;

import com.mongodb.client.result.DeleteResult;
import com.tal.note_board.dao.mongo.NoteMapper;
import com.tal.note_board.dao.pojo.Note;
import com.tal.note_board.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NoteMapperImpl implements NoteMapper {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisUtil redisUtil;
    //页面显示数量
    static final int pageSize = 10;

    /**
     * @param note
     */
    @Override
    public void saveNote(Note note) {
        //System.out.println("mongo.....并修改了redis....");
        try {
            redisUtil.delete("n:fa:1");
            redisUtil.delete("n:fbu:"+note.getUser_id()+":1");
            mongoTemplate.insert(note, "note_board");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索全部Note
     *
     * @return
     */
    @Override
    public List<Note> findAll(int page) {
        if (page == 1) {
            String key = "n:fa:" + page;
            List<Note> list = (List<Note>) redisUtil.getObject(key);

            if (list == null || list.size() == 0) {
                //System.out.println("mongo....");
                Query query = new Query();
                query.addCriteria(Criteria.where(null).is(null));
                query.skip((page - 1) * pageSize).limit(pageSize);
                query.with(Sort.by(Sort.Order.desc("gmt_create")));

                List<Note> res;
                try {
                    res = mongoTemplate.find(query, Note.class, "note_board");
                    redisUtil.setEx(key, res, 60 * 30);
                } catch (Exception e) {
                    res = null;
                    e.printStackTrace();
                }

                return res;

            } else {
                //System.out.println("redis.....");
                return list;
            }
        } else {
            //System.out.println("mongo........");
            Query query = new Query();
            query.addCriteria(Criteria.where(null).is(null));
            query.skip((page - 1) * pageSize).limit(pageSize);
            query.with(Sort.by(Sort.Order.desc("gmt_create")));
            List<Note> res;
            try {
                res = mongoTemplate.find(query, Note.class, "note_board");
            } catch (Exception e) {
                res = null;
                e.printStackTrace();
            }

            return res;
        }

    }

    /**
     * 获取对应pk_id的note
     *
     * @param user_id
     * @return List<Note>
     */
    @Override
    public List<Note> findByUserId(int user_id, int page) {
        if (page == 1) {
            String key = "n:fbu:"+user_id+":"+ page ;
            List<Note> list = (List<Note>) redisUtil.getObject(key);
            if (list == null || list.size() == 0) {
                //System.out.println("mongo........");
                //封装起来
                Query query = new Query();
                query.addCriteria(Criteria.where("user_id").is(user_id));
                query.skip((page - 1) * pageSize);
                query.limit(pageSize);
                query.with(Sort.by(Sort.Order.desc("gmt_create")));

                List<Note> res = null;
                try {
                    res = mongoTemplate.find(query, Note.class, "note_board");
                } catch (Exception e) {
                    res = null;
                    e.printStackTrace();
                }

                redisUtil.setEx(key, res, 60 * 30);
                return res;

            } else {
                //System.out.println("redis........");
                return list;
            }
        } else {
            //System.out.println("mongo.......");
            Query query = new Query();
            query.addCriteria(Criteria.where("user_id").is(user_id));
            query.skip((page - 1) * pageSize);
            query.limit(pageSize);
            query.with(Sort.by(Sort.Order.desc("gmt_create")));

            List<Note> res = null;
            try {
                res = mongoTemplate.find(query, Note.class, "note_board");
            } catch (Exception e) {
                e.printStackTrace();
                res = null;
            }
            return res;
        }

    }

    /**
     * 删除对应objectID的note
     *
     * @param _id
     */
    @Override
    public boolean deleteNoteById(int user_id, String _id) {
        //System.out.println("mongo.....查找是否有被删除数据......");
        Criteria criteria = new Criteria();
        //索引顺序
        criteria.andOperator(Criteria.where("user_id").is(user_id), Criteria.where("_id").is(_id));
        Query query = new Query(criteria);

        long deletedCount;
        try {
            DeleteResult result = mongoTemplate.remove(query, Note.class, "note_board");
            deletedCount = result.getDeletedCount();
        } catch (Exception e) {
            e.printStackTrace();
            deletedCount = 0;
        }

        if (deletedCount == 0) {
            return false;
        } else {
            //System.out.println("delete redis......");
            redisUtil.delete("n:fa:1");
            redisUtil.delete("n:fbu:"+user_id+":1");
            return true;
        }
    }
}
