package xyz.vergoclient.ui.guis;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import xyz.vergoclient.Vergo;
import xyz.vergoclient.security.ApiResponse;
import xyz.vergoclient.security.HWID;
import xyz.vergoclient.security.account.AccountUtils;
import xyz.vergoclient.ui.fonts.FontUtil;
import xyz.vergoclient.ui.fonts.JelloFontRenderer;
import xyz.vergoclient.util.*;
import xyz.vergoclient.util.datas.DataDouble5;

import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static xyz.vergoclient.ui.guis.GuiStart.*;

public class LogInGui extends GuiScreen {

    public String uidText = "UID";

    public DataDouble5 uidTextBox = new DataDouble5(), loginBox = new DataDouble5(), getHWID = new DataDouble5(), selectedDataDouble5 = null;;

    public boolean isLoggingIn = false;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        GlStateManager.pushMatrix();

        if (selectedDataDouble5 != uidTextBox && uidText.isEmpty())
            uidText = "UID";

        DisplayUtils.setCustomTitle("Authenticating...");

        Gui.drawRect(0, 0, width, height, new Color(29, 29, 29).getRGB());

        JelloFontRenderer fr = FontUtil.neurialGroteskBig;

        ScaledResolution sr = new ScaledResolution(mc);

        String authenticateText = "Authentication";

        String logInString = "Login";
        String getHWIDString = "Get HWID";

        fr.drawString(authenticateText, width/2 - fr.getStringWidth(authenticateText)/2, 20, -1);

        int loginBoxWidth = 300;
        int loginBoxHeight = 170;

        int loginButtonWidth = 90;
        int loginButtonHeight = 30;

        // Measurements
        uidTextBox.x1 = width / 2 - loginBoxWidth / 3;
        uidTextBox.x2 = width / 2 - loginBoxWidth / 3 + 200;

        uidTextBox.y1 = loginBoxHeight / 1.6f - 2;
        uidTextBox.y2 = loginBoxHeight / 1.6f - 20;

        loginBox.x1 = width / 2 - loginButtonWidth / 2;
        loginBox.x2 = width / 2 - loginButtonWidth / 2 + 90;

        loginBox.y1 = loginBoxHeight / 1.3f;
        loginBox.y2 = loginBoxHeight / 1.3f + 30;

        getHWID.x1 = width / 2 - loginButtonWidth / 2;
        getHWID.x2 = width / 2 - loginButtonWidth / 2 + 90;

        getHWID.y1 = loginBoxHeight / 1f;
        getHWID.y2 = loginBoxHeight / 1f + 30;


        RenderUtils.drawRoundedRect(width / 2 - loginBoxWidth / 2, 50, loginBoxWidth, loginBoxHeight, 3f, new Color(45, 45, 45));

        Gui.drawRect(width / 2 - loginBoxWidth / 3, loginBoxHeight / 1.6f, width / 2 - loginBoxWidth / 3 + 200, loginBoxHeight / 1.6f - 2, new Color(73, 73, 73).getRGB());
        Gui.drawRect(uidTextBox.x1, uidTextBox.y1, uidTextBox.x2, uidTextBox.y2, new Color(27, 27, 27).getRGB());

        fr.drawString(uidText, uidTextBox.x1 + 2, (float)uidTextBox.y2 + 5, new Color(255, 255, 255, 100).getRGB());

        RenderUtils.drawRoundedRect(width / 2 - loginButtonWidth / 2, loginBoxHeight / 1.3f, loginButtonWidth, loginButtonHeight, 3f, new Color(175, 51, 64));
        fr.drawString(logInString, width / 2 - loginButtonWidth / 5f, loginBoxHeight / 1.22f, -1);

        RenderUtils.drawRoundedRect(getHWID.x1, getHWID.y1, loginButtonWidth, loginButtonHeight, 3f, new Color(15, 208, 108));
        fr.drawString(getHWIDString, width/2 - fr.getStringWidth(getHWIDString)/2, loginBoxHeight / 1f + 10, -1);

        GlStateManager.popMatrix();

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        Keyboard.enableRepeatEvents(true);
        if (selectedDataDouble5 != null) {
            if (keyCode == Keyboard.KEY_V && isCtrlKeyDown()) {
                if (selectedDataDouble5 == uidTextBox)
                    uidText += getClipboardString();
            }else {
                if (keyCode == Keyboard.KEY_BACK) {

                    if (selectedDataDouble5 == uidTextBox) {
                        if (uidText.isEmpty()) {

                        }
                        else if (uidText.length() > 0) {
                            uidText = uidText.substring(0, uidText.length() - 1);
                        }
                    }

                }
                else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {

                    if (selectedDataDouble5 == uidTextBox)
                        uidText += Character.toString(typedChar);

                }

            }

        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if (Mouse.isInsideWindow() && mouseButton == 0) {

            if (GuiUtils.isMouseOverDataDouble5(mouseX, mouseY, loginBox) && !isLoggingIn) {
                if(uidText == "UID" || uidText == null) {
                    System.out.println("Please enter a UID.");
                    return;
                }
                new Thread(() -> {
                    try {
                        isLoggingIn = true;
                        String response = NetworkManager.getNetworkManager().sendPost(new HttpPost("https://vergoclient.xyz/api/authentication.php?usersIdentificationNumber=" + uidText + "&hardwareIDCheckValue=" + Base64.getEncoder().encodeToString(HWID.getHWIDForWindows().getBytes())));
                        ApiResponse apiResponse = MiscellaneousUtils.parseApiResponse(response);
                        if(apiResponse.status == ApiResponse.ResponseStatus.FORBIDDEN) {
                            System.out.println("You have been banned from using Vergo! Please contact the developers in our Discord.");
                            mc.shutdown();
                            return;
                        } else
                        if (apiResponse.status == ApiResponse.ResponseStatus.OK) {
                            AccountUtils.account = MiscellaneousUtils.parseAccount(new JSONObject(response).toString());
                            new Thread(() -> {
                                while (true) {
                                    try {
                                        ApiResponse apiResponse1 = MiscellaneousUtils.parseApiResponse(NetworkManager.getNetworkManager().sendPost(new HttpPost("https://vergoclient.xyz/api/authentication.php"), new BasicNameValuePair("uid",AccountUtils.account.uid + ""), new BasicNameValuePair("hwid", AccountUtils.account.hwid), new BasicNameValuePair("banned", AccountUtils.account.banned + "")));
                                        System.out.println(apiResponse1);
                                        if (apiResponse1.status == ApiResponse.ResponseStatus.OK) {
                                            break;
                                        }
                                        Thread.sleep(10000);
                                    } catch (Exception e) {

                                    }
                                }
                            }).start();
                            mc.displayGuiScreen(new GuiStart());
                            hasLoaded = true;
                            Keyboard.enableRepeatEvents(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isLoggingIn = false;
                }).start();
            }

            else if (GuiUtils.isMouseOverDataDouble5(mouseX, mouseY, uidTextBox)) {
                selectedDataDouble5 = uidTextBox;
                if (uidText.equals("UID")) {
                    uidText = "";
                }
            }

            else if (GuiUtils.isMouseOverDataDouble5(mouseX, mouseY, getHWID)) {
                selectedDataDouble5 = getHWID;
                try {
                    setClipboardString(HWID.getHWIDForWindows());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }

            else {
                selectedDataDouble5 = null;
            }
        }

    }

}
