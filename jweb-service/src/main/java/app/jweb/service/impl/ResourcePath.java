package app.jweb.service.impl;


import app.jweb.ApplicationException;
import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
public class ResourcePath {
    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("(\\w+)(:.+)?");
    public final String path;
    public final List<String> variableNames = Lists.newArrayList();
    public final List<Node> nodes = Lists.newArrayList();

    public ResourcePath(String path) {
        if (Strings.isNullOrEmpty(path) || path.charAt(0) != '/') {
            throw new ApplicationException("path must start with /");
        }

        this.path = path;
        nodes.add(new Node("/"));
        StringBuilder b = new StringBuilder();

        int state = 0;
        for (int i = 1; i < path.length(); i++) {
            char c = path.charAt(i);
            if (state == 0 && c == '{') {
                state = 1;
            } else if (state == 1 && c == '}') {
                nodes.add(pathVariable(b.toString()));
                b.delete(0, b.length());
                state = 0;
            } else if (c == '/') {
                nodes.add(new Node(b.toString()));
                b.delete(0, b.length());
                nodes.add(new Node("/"));
                state = 0;
            } else if (c == '?') {
                if (b.length() > 0) {
                    nodes.add(new Node(b.toString()));
                    b.delete(0, b.length());
                }
                break;
            } else if (i == path.length() - 1) {
                b.append(c);
                nodes.add(new Node(b.toString()));
            } else {
                b.append(c);
            }
        }
    }

    public String format(Map<String, String> pathParams) {
        try {
            int index = 0;
            StringBuilder b = new StringBuilder();
            for (Node node : nodes) {
                if (node instanceof Variable) {
                    Variable variable = (Variable) node;
                    String value = pathParams.get(variableNames.get(index++));
                    if (value == null) {
                        throw new ApplicationException("missing path variable, path={}, variable={}", path, variable.path);
                    }
                    b.append(URLEncoder.encode(value, Charsets.UTF_8.name()));
                } else {
                    b.append(node.path);
                }
            }
            return b.toString();
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationException(e);
        }
    }

    public String format(String... args) {
        try {
            int index = 0;
            StringBuilder b = new StringBuilder();
            for (Node node : nodes) {
                if (node instanceof Variable) {
                    Variable variable = (Variable) node;
                    String value = args[index++];
                    if (value == null) {
                        throw new ApplicationException("missing path variable, path={}, variable={}", path, variable.path);
                    }
                    b.append(URLEncoder.encode(value, Charsets.UTF_8.name()));
                } else {
                    b.append(node.path);
                }
            }
            return b.toString();
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationException(e);
        }
    }

    public Map<String, String> pathParams(ResourcePath resourcePath) {
        try {
            Map<String, String> pathParams = Maps.newHashMap();
            int index = 0;
            for (int i = 0; i < nodes.size(); i++) {
                Node template = nodes.get(i);
                Node value = resourcePath.nodes.get(i);
                if (template instanceof Variable) {
                    pathParams.put(variableNames.get(index), URLDecoder.decode(value.path, Charsets.UTF_8.name()));
                    index++;
                }
            }
            return pathParams;
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationException(e);
        }
    }

    public boolean hasVariableNode() {
        return !variableNames.isEmpty();
    }

    private Variable pathVariable(String path) {
        Matcher matcher = PATH_VARIABLE_PATTERN.matcher(path);
        if (!matcher.matches()) {
            throw new Error("invalid path variable " + path);
        }
        variableNames.add(matcher.group(1));
        if (matcher.group(2) != null) {
            return new Variable(matcher.group(2));
        } else {
            return new Variable(".*");
        }
    }


    public static class Node {
        public final String path;
        public final NodeType type;

        public Node(String path, NodeType type) {
            this.path = path;
            this.type = type;
        }

        public Node(String path) {
            this(path, NodeType.TEXT);
        }

        public boolean matches(String path) {
            return this.path.equals(path);
        }

        public int priority() {
            return Integer.MAX_VALUE;
        }

        public boolean isEquals(Node node) {
            return Objects.equal(path, node.path) && Objects.equal(type, node.type);
        }
    }

    public enum NodeType {
        TEXT, VARIABLE, WILDCARD
    }


    static class Variable extends Node {
        final Pattern pattern;

        Variable(String regex) {
            super(regex, NodeType.VARIABLE);
            pattern = Pattern.compile(regex);
        }

        @Override
        public boolean matches(String path) {
            return pattern.matcher(path).matches();
        }

        @Override
        public int priority() {
            return pattern.pattern().length();
        }
    }
}
