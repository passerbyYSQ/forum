package top.ysqorz.forum.po;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "db_file")
public class DbFile {
    @Id
    private Integer id;

    /**
     * 上传时的原始文件名
     */
    @Column(name = "origin_name")
    private String originName;

    /**
     * 上传后生成的随机文件名
     */
    @Column(name = "rand_name")
    private String randName;

    /**
     * 文件的md5
     */
    @Column(name = "file_md5")
    private String fileMd5;

    /**
     * 总字节数
     */
    @Column(name = "total_bytes")
    private Long totalBytes;

    /**
     * 已上传的字节数
     */
    @Column(name = "uploaded_bytes")
    private Long uploadedBytes;

    /**
     * 第一次上传该文件的用户id
     */
    @Column(name = "creator_id")
    private Integer creatorId;

    /**
     * 文件在服务器本地的路径
     */
    @Column(name = "local_path")
    private String localPath;

    /**
     * 云存储
     */
    @Column(name = "oss_url")
    private String ossUrl;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
