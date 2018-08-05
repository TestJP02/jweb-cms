package io.sited.file.web.component;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.sited.file.api.DirectoryWebService;
import io.sited.file.api.directory.DirectoryResponse;
import io.sited.file.api.file.FileListQuery;
import io.sited.file.web.service.FileService;
import io.sited.file.web.web.ajax.directory.DirectoryBreadAJAXResponse;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.web.AbstractWebComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class FileComponent extends AbstractWebComponent {
    @Inject
    DirectoryWebService directoryService;
    @Inject
    FileService fileService;

    public FileComponent() {
        super("file", "file/include/file.list.html", Lists.newArrayList());
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {

        attributes.forEach(bindings::put);
        String pathAttribute = (String) attributes.get("path");

        String directoryId = null;
        if (!Strings.isNullOrEmpty(pathAttribute)) {
            Optional<DirectoryResponse> optional = directoryService.findByPath(pathAttribute);
            if (optional.isPresent()) {
                directoryId = optional.get().id;
            }
        }
        DirectoryResponse directory;
        if (Strings.isNullOrEmpty(directoryId)) {
            directory = directoryService.findByPath("/").get();
        } else {
            directory = directoryService.get(directoryId);
        }
        bindings.put("directory", directory);
        List<DirectoryBreadAJAXResponse> breads = Lists.newArrayList();
        StringBuilder path = new StringBuilder("/");
        for (String displayName : directory.path.split("\\/")) {
            if (Strings.isNullOrEmpty(displayName)) {
                continue;
            }
            path.append(displayName).append('/');
            DirectoryBreadAJAXResponse directoryBreadAJAXResponse = new DirectoryBreadAJAXResponse();
            directoryBreadAJAXResponse.displayName = displayName;
            directoryBreadAJAXResponse.path = path.toString();
            breads.add(directoryBreadAJAXResponse);
        }
        bindings.put("breads", ImmutableList.copyOf(breads));

//        directoryService.validate(user, directory.id);
        FileListQuery query = new FileListQuery();
        query.query = (String) attributes.get("query");
        query.directoryId = directory.id;
        query.limit = 10;
        query.page = 1;
        String page = (String) attributes.get("page");
        if (!Strings.isNullOrEmpty(page)) {
            query.page = Integer.parseInt(page);
        }
        bindings.put("files", fileService.list(query));

        StringBuilder fullPath = new StringBuilder("/file/");
        if (!Strings.isNullOrEmpty(pathAttribute) || !Strings.isNullOrEmpty(query.query)) {
            buildFullPath(fullPath, pathAttribute, query);
        }
        bindings.put("fullPath", fullPath.toString());
        template().output(bindings, out);
    }

    private void buildFullPath(StringBuilder fullPath, String pathAttribute, FileListQuery query) {
        fullPath.append('?');
        if (!Strings.isNullOrEmpty(pathAttribute)) {
            fullPath.append("path=").append(pathAttribute);
        }
        if (!Strings.isNullOrEmpty(query.query)) {
            if (!Strings.isNullOrEmpty(pathAttribute)) {
                fullPath.append('&');
            }
            fullPath.append("query=").append(query.query);
        }
    }
}
