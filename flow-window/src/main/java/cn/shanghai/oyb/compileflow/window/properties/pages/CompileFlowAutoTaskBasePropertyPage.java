/*
 * Copyright (c) 2021. Ou Yubin
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.shanghai.oyb.compileflow.window.properties.pages;

import cn.shanghai.oyb.compileflow.common.commands.AddCommand;
import cn.shanghai.oyb.compileflow.common.commands.DeleteCommand;
import cn.shanghai.oyb.compileflow.common.commands.SetCommand;
import cn.shanghai.oyb.compileflow.common.model.query.utils.PsiElementQueryUtil;
import cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants;
import cn.shanghai.oyb.compileflow.ui.models.CompileFlowMethodArgsModel;
import cn.shanghai.oyb.compileflow.ui.renderer.CompileFlowAutoTaskMethodListRenderer;
import cn.shanghai.oyb.compileflow.ui.renderer.CompileFlowTypeListRenderer;
import cn.shanghai.oyb.compileflow.ui.table.CompileFlowAutoTaskMethodArgsTable;
import cn.shanghai.oyb.compileflow.ui.table.cell.editor.CompileFlowClassCellEditor;
import cn.shanghai.oyb.compileflow.ui.table.cell.editor.CompileFlowOptionsCellEditor;
import cn.shanghai.oyb.compileflow.ui.table.models.CompileFlowPropertyModel;
import cn.shanghai.oyb.flow.core.dialogs.TreeClassChooserDialog;
import cn.shanghai.oyb.flow.core.editor.commands.GraphEditCommandStack;
import cn.shanghai.oyb.flow.core.editor.editdomain.GraphEditDomain;
import cn.shanghai.oyb.flow.core.models.XmlTagModelElement;
import cn.shanghai.oyb.flow.core.swing.CommonTabbedPane;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.Query;
import com.intellij.util.ui.JBEmptyBorder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;
import icons.CompileFlowIcons;
import icons.CompileFlowSvgImages;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.NAME_ATTR_NAME;
import static cn.shanghai.oyb.compileflow.model.constants.CompileFlowXmlTagConstants.TYPE_ATTR_NAME;

/**
 * 任务属性页
 *
 * @author ouyubin
 */
public class CompileFlowAutoTaskBasePropertyPage extends CompileFlowElementBasePropertyPage {

    private static final Logger LOG = Logger.getInstance(CompileFlowAutoTaskBasePropertyPage.class);

    private JBTextField nameTextField;

    private ComboBox<String> typeComboBox;

    private JBTextField classTextField;

    private CompileFlowAutoTaskMethodArgsTable myCompileFlowAutoTaskMethodArgsTable;

    private JButton classButton;

    private ComboBox<String> methodComboBox;

    private JBTextField beanTextField;

    private JBLabel beanLabel;

    private MethodComboBoxActionListener methodComboBoxActionListener;

    private DocumentListener myDocumentListener;

    public CompileFlowAutoTaskBasePropertyPage() {
        super();
    }

    /**
     * @param parent
     */
    @Override
    public void createControl(JComponent parent) {
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(0);
        this.setLayout(borderLayout);

        FormBuilder formBuilder = FormBuilder.create();
        ComponentFactory componentFactory = FormsSetup.getComponentFactoryDefault();
        formBuilder.factory(componentFactory);
        JLabel titleLabel = new JLabel("任务 «AutoTask»", new ImageIcon(CompileFlowSvgImages.AUTO_TASK_IMAGE), SwingConstants.LEFT);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, UIUtil.getFontSize(UIUtil.FontSize.NORMAL) + JBUI.scale(2f)));

        FormLayout titleLayout = new FormLayout(
                DEFAULT_PROPERTY_PAGE_COLUMN_MARGIN() + ",pref," + DEFAULT_PROPERTY_PAGE_COLUMN_SPEC() + ",pref,pref:grow," +
                        "pref," + DEFAULT_PROPERTY_PAGE_COLUMN_MARGIN(),
                DEFAULT_PROPERTY_PAGE_TITLE_ROW_TOP_MARGIN() + ",pref," + DEFAULT_PROPERTY_PAGE_TITLE_ROW_SPEC() + ",pref");
        JPanel panel = formBuilder.layout(titleLayout).add(titleLabel).xyw(2, 2, 4)
                .addSeparator(null).xyw(1, 4, 7)
                .build();
        this.add(panel, BorderLayout.NORTH);

        CommonTabbedPane tabbedPane = createTabPanel();
        JPanel propertyPanel = createAttributesPanel();
        propertyPanel.setBackground(UIUtil.getPanelBackground());
        propertyPanel.setBorder(new JBEmptyBorder(0));
        tabbedPane.addTab("常规", CompileFlowIcons.COMMON_ICON, propertyPanel);
        JPanel othersPanel = createOthersPanel();
        tabbedPane.addTab("其他", CompileFlowIcons.OTHERS_ICON, othersPanel);
        this.add(tabbedPane, BorderLayout.CENTER);
    }


    private CommonTabbedPane createTabPanel() {
        return new CommonTabbedPane();
    }


    private JBPanel createAttributesPanel() {
        JBPanel jbPanel = new JBPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        jbPanel.setLayout(gridBagLayout);
        FormLayout layout = new FormLayout(
                DEFAULT_PROPERTY_PAGE_COLUMN_MARGIN() + ",pref," + DEFAULT_PROPERTY_PAGE_COLUMN_SPEC() + ",pref,pref:grow," + DEFAULT_PROPERTY_PAGE_COLUMN_SPEC() +
                        ",pref," + DEFAULT_PROPERTY_PAGE_COLUMN_SPEC() + ",pref:grow,pref," + DEFAULT_PROPERTY_PAGE_COLUMN_MARGIN(),
                DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",pref," + DEFAULT_PROPERTY_PAGE_ROW_SPEC() + ",fill:pref:grow," + DEFAULT_PROPERTY_PAGE_ROW_SPEC());

        FormBuilder formBuilder = FormBuilder.create();
        ComponentFactory componentFactory = FormsSetup.getComponentFactoryDefault();
        formBuilder.factory(componentFactory);
        JBScrollPane argsScrollPane = new JBScrollPane();

        CompileFlowAutoTaskMethodListRenderer renderer = new CompileFlowAutoTaskMethodListRenderer();
        methodComboBox = new ComboBox<>();
        methodComboBox.setRenderer(renderer);

        CompileFlowTypeListRenderer typeRenderer = new CompileFlowTypeListRenderer();
        typeComboBox = new ComboBox<>();
        typeComboBox.addItem("java");
        typeComboBox.addItem("spring-bean");
        typeComboBox.setRenderer(typeRenderer);

        myCompileFlowAutoTaskMethodArgsTable = new CompileFlowAutoTaskMethodArgsTable(new CompileFlowMethodArgsModel(this));
        //setJTableColumnsWidth(componentServiceArgsTable, 480, 30, 70);

        HyperlinkLabel classHyperLinkLabel = new HyperlinkLabel("类:");

        JPanel panel = formBuilder.layout(layout).
                add(((DefaultComponentFactory) componentFactory).createSeparator(createSeparatorLabel("基本"))).xyw(2, 2, 9).
                add("名称:").xy(4, 4).
                add(((DefaultComponentFactory) componentFactory).createSeparator(createSeparatorLabel("动作"))).xyw(2, 6, 9).
                add("类型:").xy(4, 8).
                add(beanLabel = new JBLabel("Bean:")).xy(7, 8).
                add(classHyperLinkLabel).xy(4, 10, "right,center").
                add("方法:").xy(4, 12).
                add("参数:").xy(4, 14).
                add(nameTextField = new JBTextField()).xyw(5, 4, 6).
                add(typeComboBox).xy(5, 8).
                add(beanTextField = new JBTextField()).xyw(9, 8, 2).
                add(classTextField = new JBTextField(16)).xyw(5, 10, 5).
                add(classButton = new JButton("浏览...")).xy(10, 10).
                add(methodComboBox).xyw(5, 12, 6).
                add(argsScrollPane).xyw(4, 16, 7).
                build();

        classTextField.setEditable(false);
        Dimension dimension = argsScrollPane.getViewport().getPreferredSize();
        myCompileFlowAutoTaskMethodArgsTable.setPreferredScrollableViewportSize(dimension);
        myCompileFlowAutoTaskMethodArgsTable.setFillsViewportHeight(true);
        argsScrollPane.setViewportView(myCompileFlowAutoTaskMethodArgsTable);

        classHyperLinkLabel.addHyperlinkListener((event) -> {

            Object object = myInput();
            if (object != null) {
                if (object instanceof XmlTagModelElement) {
                    XmlTag xmlTag = ((XmlTagModelElement) object).getXmlTag();
                    Project project = xmlTag.getProject();
                    String qualifiedName = classTextField.getText();

                    if (StringUtils.isEmpty(qualifiedName)) {
                        return;
                    }

                    PsiClass selectedClass = JavaPsiFacade.getInstance(project).findClass(qualifiedName,
                            GlobalSearchScope.everythingScope(project));
                    if (selectedClass == null) {
                        return;
                    }
                    FileEditorManager.getInstance(project).openFile(selectedClass.getContainingFile().getVirtualFile(), true);
                }
            }
        });

        myDocumentListener = new DocumentListener(NAME_ATTR_NAME);
        nameTextField.getDocument().addDocumentListener(myDocumentListener);

        classButton.addActionListener((event) ->
        {
            Object object = myInput();
            if (object != null) {
                if (object instanceof XmlTagModelElement) {
                    XmlTag xmlTag = ((XmlTagModelElement) object).getXmlTag();
                    Project project = xmlTag.getProject();
                    TreeClassChooserDialog treeClassChooserDialog = new TreeClassChooserDialog("选择Class", project, GlobalSearchScope.everythingScope(project), PsiClass.class,
                            element -> {
                                if (element instanceof PsiClass) {
                                    return ((PsiClass) element).getParent() instanceof PsiJavaFile;
                                } else {
                                    return false;
                                }
                            }
                            , null);
                    treeClassChooserDialog.showDialog();
                    PsiClass selectedClass = (PsiClass) treeClassChooserDialog.getSelected();

                    String qualifiedName = selectedClass.getQualifiedName();
                    if (StringUtils.isEmpty(qualifiedName)) {
                        return;
                    }
                    classTextField.setText(qualifiedName);
                    PsiMethod[] methods = selectedClass.getMethods();
                    methodComboBox.removeActionListener(methodComboBoxActionListener);
                    methodComboBox.removeAllItems();
                    Arrays.stream(methods).forEach(method -> {
                        if (!method.isConstructor()) {
                            String methodName = method.getName();
                            methodComboBox.addItem(methodName);
                        }
                    });
                    methodComboBox.addActionListener(methodComboBoxActionListener);
                    java.util.List actionElements = Arrays.stream(xmlTag.getChildren()).
                            filter(element -> (element instanceof XmlTag && ((XmlTag) element).getName().equals(CompileFlowXmlTagConstants.ACTION_TAG_NAME))).collect(Collectors.toList());
                    if (!actionElements.isEmpty()) {
                        XmlTag actionXmlTag = (XmlTag) actionElements.get(0);

                        java.util.List actionHandleElements = PsiElementQueryUtil.getPsiElementByTagName(actionXmlTag, CompileFlowXmlTagConstants.ACTION_HANDLE_TAG_NAME);
                        if (actionHandleElements.isEmpty()) {
                            XmlTag actionHandleXmlTag = actionXmlTag.createChildTag(CompileFlowXmlTagConstants.ACTION_HANDLE_TAG_NAME, null, null, false);
                            actionHandleXmlTag.setAttribute("clazz", classTextField.getText());

                            GraphEditDomain editDomain = (GraphEditDomain) getAdapter().getAdapter(GraphEditDomain.class);
                            GraphEditCommandStack commandStack = editDomain.getCommandStack();
                            AddCommand addCommand = new AddCommand(getAdapter(), actionXmlTag,
                                    actionHandleXmlTag);
                            commandStack.execute(addCommand);


                        } else {
                            XmlTag actionHandleXmlTag = (XmlTag) actionHandleElements.get(0);
                            GraphEditDomain editDomain = (GraphEditDomain) getAdapter().getAdapter(GraphEditDomain.class);
                            GraphEditCommandStack commandStack = editDomain.getCommandStack();
                            SetCommand lftSetCommand = new SetCommand(getAdapter(), actionHandleXmlTag,
                                    "clazz"
                                    , classTextField.getText());
                            commandStack.execute(lftSetCommand);
//                            lftSetCommand = new LFTSetCommand(getAdapter(), actionHandleXmlTag,
//                                    "method"
//                                    , null);
//                            lftCommandStack.execute(lftSetCommand);

                            //--批量清扫已有节点
                            XmlTag[] varXmlTags = actionHandleXmlTag.findSubTags(CompileFlowXmlTagConstants.VAR_TAG_NAME);
                            for (XmlTag varXmlTag : varXmlTags) {
                                DeleteCommand varRemoveCommand = new DeleteCommand(getAdapter(), varXmlTag);
                                commandStack.execute(varRemoveCommand);
                            }

                        }
                    }

                }
            }
        });

        //--类型控制
        typeComboBox.addActionListener((event) -> {
            Object item = typeComboBox.getSelectedItem();
            Object object = myInput();
            XmlTag xmlTag = ((XmlTagModelElement) object).getXmlTag();
            beanLabel.setVisible(StringUtils.equals((String) item, "spring-bean"));
            beanTextField.setVisible(StringUtils.equals((String) item, "spring-bean"));
            java.util.List actionElements = Arrays.stream(xmlTag.getChildren()).
                    filter(element -> (element instanceof XmlTag && ((XmlTag) element).getName().equals(CompileFlowXmlTagConstants.ACTION_TAG_NAME))).collect(Collectors.toList());
            if (actionElements.isEmpty()) {
                XmlTag actionXmlTag = xmlTag.createChildTag(CompileFlowXmlTagConstants.ACTION_TAG_NAME, null, null, false);
                actionXmlTag.setAttribute("type", String.valueOf(item));
                GraphEditDomain editDomain = (GraphEditDomain) getAdapter().getAdapter(GraphEditDomain.class);
                GraphEditCommandStack commandStack = editDomain.getCommandStack();
                AddCommand addCommand = new AddCommand(getAdapter(), xmlTag,
                        actionXmlTag);
                commandStack.execute(addCommand);
            } else {
                XmlTag actionXmlTag = (XmlTag) actionElements.get(0);
                GraphEditDomain componentEditDomain = (GraphEditDomain) getAdapter().getAdapter(GraphEditDomain.class);
                GraphEditCommandStack componentCommandStack = componentEditDomain.getCommandStack();
                SetCommand lftSetCommand = new SetCommand(getAdapter(), actionXmlTag,
                        "type"
                        , (String) item);
                componentCommandStack.execute(lftSetCommand);
            }
        });

        methodComboBoxActionListener = new MethodComboBoxActionListener();
        methodComboBox.addActionListener(methodComboBoxActionListener);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = JBUI.insets(0);
        jbPanel.add(panel, gbc);
        return jbPanel;
    }

    class MethodComboBoxActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            Object item = methodComboBox.getSelectedItem();
            Object object = myInput();
            XmlTag xmlTag = ((XmlTagModelElement) object).getXmlTag();
            Project project = xmlTag.getProject();
            PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(classTextField.getText(), GlobalSearchScope.everythingScope(project));
            PsiMethod psiMethod = Arrays.stream(psiClass.getMethods()).filter(method -> method.getName().equals(item)).findFirst().get();
            PsiParameterList parameterList = psiMethod.getParameterList();

            Query<PsiReference> search = ReferencesSearch.search(psiMethod);
            search.forEach(reference -> {
                PsiElement psiElement = reference.getElement();
            });


            //--开始写入对应节点
            if (parameterList.getChildren().length != 0) {
                List<PsiElement> actionElements = Arrays.stream(xmlTag.getChildren()).
                        filter(element -> (element instanceof XmlTag && ((XmlTag) element).getName().equals(CompileFlowXmlTagConstants.ACTION_TAG_NAME))).collect(Collectors.toList());
                XmlTag actionXmlTag = (XmlTag) actionElements.get(0);

                java.util.List actionHandles = PsiElementQueryUtil.getPsiElementByTagName(actionXmlTag, CompileFlowXmlTagConstants.ACTION_HANDLE_TAG_NAME);
                if (!actionHandles.isEmpty()) {
                    XmlTag actionHandleXmlTag = (XmlTag) actionHandles.get(0);
                    GraphEditDomain editDomain = (GraphEditDomain) getAdapter().getAdapter(GraphEditDomain.class);
                    GraphEditCommandStack commandStack = editDomain.getCommandStack();
                    SetCommand lftSetCommand = new SetCommand(getAdapter(), actionHandleXmlTag,
                            "method", (String) item);
                    commandStack.execute(lftSetCommand);

                    //--批量清扫已有节点
                    XmlTag[] varXmlTags = actionHandleXmlTag.findSubTags(CompileFlowXmlTagConstants.VAR_TAG_NAME);
                    for (XmlTag varXmlTag : varXmlTags) {
                        DeleteCommand varRemoveCommand = new DeleteCommand(getAdapter(), varXmlTag);
                        commandStack.execute(varRemoveCommand);
                    }


                    Arrays.stream(parameterList.getParameters()).forEach(param -> {
                        XmlTag varXmlTag =
                                actionHandleXmlTag.createChildTag(CompileFlowXmlTagConstants.VAR_TAG_NAME, null, null, false);
                        varXmlTag.setAttribute("name", "input");
                        PsiType psiType = param.getType();
                        String argTypeName = null;
                        if (psiType instanceof PsiClassType) {
                            PsiClassType pct = (PsiClassType) psiType;
                            final PsiClass paramPsiClass = pct.resolve();
                            argTypeName = paramPsiClass != null ? paramPsiClass.getQualifiedName() : null;
                        } else if (psiType instanceof PsiPrimitiveType) {
                            argTypeName = ((PsiPrimitiveType) psiType).getBoxedTypeName();
                        }
                        varXmlTag.setAttribute("dataType", argTypeName);
                        varXmlTag.setAttribute("inOutType", "param");
                        AddCommand varAddCommand = new AddCommand(getAdapter(), actionHandleXmlTag,
                                varXmlTag);
                        commandStack.execute(varAddCommand);
                    });

                    XmlTag varXmlTag =
                            actionHandleXmlTag.createChildTag(CompileFlowXmlTagConstants.VAR_TAG_NAME, null, null, false);
                    String returnTypeName = "";
                    PsiType returnPsiType = psiMethod.getReturnType();
                    if (returnPsiType instanceof PsiClassType) {
                        PsiClassType pct = (PsiClassType) returnPsiType;
                        final PsiClass paramPsiClass = pct.resolve();
                        returnTypeName = paramPsiClass != null ? paramPsiClass.getQualifiedName() : null;
                    } else if (returnPsiType instanceof PsiPrimitiveType) {
                        returnTypeName = ((PsiPrimitiveType) returnPsiType).getBoxedTypeName();
                    }
                    varXmlTag.setAttribute("name", "output");
                    varXmlTag.setAttribute("dataType", returnTypeName);
                    varXmlTag.setAttribute("inOutType", "return");
                    AddCommand varAddCommand = new AddCommand(getAdapter(), actionHandleXmlTag,
                            varXmlTag);
                    commandStack.execute(varAddCommand);

                }
            }
        }
    }


    @Override
    public void refresh() {
        super.refresh();
        Object object = myInput();
        if (object != null) {
            if (object instanceof XmlTagModelElement) {
                object = ((XmlTagModelElement) object).getXmlTag();
                if (((XmlTag) object).isValid()) {
                    nameTextField.getDocument().removeDocumentListener(myDocumentListener);
                    String name = ((XmlTag) object).getAttribute(NAME_ATTR_NAME) == null ? "" : ((XmlTag) object).getAttribute(NAME_ATTR_NAME).getValue();
                    if (!StringUtils.equals(name, nameTextField.getText())) {
                        nameTextField.setText(name);
                    }
                    nameTextField.getDocument().addDocumentListener(myDocumentListener);

                    java.util.List<PsiElement> actionXmlTags = PsiElementQueryUtil.getPsiElementByTagName((PsiElement) object, CompileFlowXmlTagConstants.ACTION_TAG_NAME);
                    if (!actionXmlTags.isEmpty()) {
                        XmlTag actionXmlTag = (XmlTag) actionXmlTags.get(0);
                        String type = actionXmlTag.getAttribute(TYPE_ATTR_NAME) == null ? "" : actionXmlTag.getAttribute(TYPE_ATTR_NAME).getValue();
                        if (!StringUtils.equals((String) typeComboBox.getSelectedItem(), type)) {
                            typeComboBox.setSelectedItem(type);
                        }
                        beanLabel.setVisible(!StringUtils.equals(type, "java"));
                        beanTextField.setVisible(!StringUtils.equals(type, "java"));

                        java.util.List<PsiElement> actionHandleXmlTags = PsiElementQueryUtil.getPsiElementByTagName(actionXmlTag, CompileFlowXmlTagConstants.ACTION_HANDLE_TAG_NAME);
                        if (!actionHandleXmlTags.isEmpty()) {
                            XmlTag actionHandleXmlTag = (XmlTag) actionHandleXmlTags.get(0);
                            String clazz = actionHandleXmlTag.getAttribute("clazz") == null ? "" : actionHandleXmlTag.getAttribute("clazz").getValue();
                            classTextField.setText(clazz);

                            //--设置方法列表
                            String method = actionHandleXmlTag.getAttribute("method") == null ? "" : actionHandleXmlTag.getAttribute("method").getValue();

                            Project project = ((XmlTag) object).getProject();
                            if (!StringUtils.isEmpty(clazz)) {
                                PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(clazz, GlobalSearchScope.everythingScope(project));
                                PsiMethod[] methods = psiClass.getMethods();

                                methodComboBox.removeActionListener(methodComboBoxActionListener);
                                methodComboBox.removeAllItems();
                                Arrays.stream(methods).forEach(curMethod -> {
                                    if (!curMethod.isConstructor()) {
                                        String methodName = curMethod.getName();
                                        methodComboBox.addItem(methodName);
                                    }
                                });
                                methodComboBox.setSelectedItem(method);
                                methodComboBox.addActionListener(methodComboBoxActionListener);
                                List<PsiElement> varXmlTags = PsiElementQueryUtil.getPsiElementByTagName(actionHandleXmlTag, CompileFlowXmlTagConstants.VAR_TAG_NAME);

                                java.util.List<CompileFlowPropertyModel<Object>> args = new ArrayList();
                                if (!varXmlTags.isEmpty()) {
                                    for (Object var : varXmlTags) {
                                        if (var instanceof XmlTag) {
                                            args.add(new CompileFlowPropertyModel(var));
                                        }
                                    }
                                }
                                CompileFlowMethodArgsModel compileFlowMethodArgsModel = (CompileFlowMethodArgsModel) myCompileFlowAutoTaskMethodArgsTable.getModel();
                                myCompileFlowAutoTaskMethodArgsTable.getColumnModel().getColumn(1).setCellEditor(new CompileFlowClassCellEditor(adapter()));
                                if (compileFlowMethodArgsModel.getAdapter() == null)
                                    compileFlowMethodArgsModel.setAdapter(adapter());
                                compileFlowMethodArgsModel.setArgs(args);


                                XmlTag parent = ((XmlTag) object).getParentTag();
                                List vars = PsiElementQueryUtil.getPsiElementByTagName(parent, CompileFlowXmlTagConstants.VAR_TAG_NAME);
                                List<String> contextParamNames = new ArrayList<>();
                                for (Object var : vars) {
                                    String varName = ((XmlTag) var).getAttributeValue("name");
                                    if (!StringUtils.isEmpty(varName)) {
                                        contextParamNames.add(varName);
                                    }
                                }
                                myCompileFlowAutoTaskMethodArgsTable.getColumnModel().getColumn(5).setCellEditor(new CompileFlowOptionsCellEditor(contextParamNames.toArray(new String[0])));

                            } else {
                                methodComboBox.removeActionListener(methodComboBoxActionListener);
                                methodComboBox.removeAllItems();
                                methodComboBox.addActionListener(methodComboBoxActionListener);
                                CompileFlowMethodArgsModel compileFlowMethodArgsModel = (CompileFlowMethodArgsModel) myCompileFlowAutoTaskMethodArgsTable.getModel();
                                compileFlowMethodArgsModel.setArgs(Collections.emptyList());
                            }
                        } else {
                            classTextField.setText(null);
                            methodComboBox.removeActionListener(methodComboBoxActionListener);
                            methodComboBox.removeAllItems();
                            methodComboBox.addActionListener(methodComboBoxActionListener);
                            CompileFlowMethodArgsModel compileFlowMethodArgsModel = (CompileFlowMethodArgsModel) myCompileFlowAutoTaskMethodArgsTable.getModel();
                            compileFlowMethodArgsModel.setArgs(Collections.emptyList());
                        }
                    }
                }
            }
        }
    }

}

